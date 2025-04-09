package com.flipperdevices.bsb.timer.active.api

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.active.composable.TimerBusyComposableScreen
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.PauseFullScreenOverlayComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.stop.StopSessionSheetDecomposeComponentImpl
import com.flipperdevices.bsb.timer.controller.TimerControllerApi
import com.flipperdevices.bsb.timer.focusdisplay.api.FocusDisplayDecomposeComponent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class ActiveTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    stopSessionSheetDecomposeComponentFactory: (
        componentContext: ComponentContext,
        onConfirm: () -> Unit
    ) -> StopSessionSheetDecomposeComponentImpl,
    focusDisplayDecomposeComponentFactory: FocusDisplayDecomposeComponent.Factory,
    private val metricApi: MetricApi,
    private val timerControllerApi: TimerControllerApi
) : ActiveTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    init {
        focusDisplayDecomposeComponentFactory.invoke(lifecycle = lifecycle)
    }

    private val stopSessionSheetDecomposeComponent = stopSessionSheetDecomposeComponentFactory.invoke(
        childContext("busy_stopSessionSheetDecomposeComponent"),
        {
            timerApi.getTimestampState().value
                .runningOrNull
                ?.let { running -> Clock.System.now().minus(running.start) }
                ?.let { timePassed ->
                    metricApi.reportEvent(BEvent.TimerAborted(timePassed.inWholeMilliseconds))
                }

            timerControllerApi.stop()
        }
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerApi.getState().collectAsState()
        val hazeState = remember { HazeState() }
        when (val state = state) {
            is ControlledTimerState.InProgress.Await,
            ControlledTimerState.NotStarted,
            is ControlledTimerState.Finished -> Unit

            is ControlledTimerState.InProgress.Running -> {
                TimerBusyComposableScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .hazeSource(hazeState),
                    state = state,
                    onSkip = {
                        timerApi.getTimestampState().value
                            .runningOrNull
                            ?.let { running -> Clock.System.now().minus(running.noOffsetStart) }
                            ?.let { timePassed ->
                                metricApi.reportEvent(BEvent.TimerSkipped(timePassed.inWholeMilliseconds))
                            }

                        timerControllerApi.skip()
                    },
                    onPauseClick = {
                        timerControllerApi.pause()
                    },
                    onBack = {
                        stopSessionSheetDecomposeComponent.show()
                    }
                )
                if (state.isOnPause) {
                    PauseFullScreenOverlayComposable(
                        onStartClick = {
                            timerControllerApi.resume()
                        }
                    )
                }
            }
        }

        stopSessionSheetDecomposeComponent.Render(hazeState)
    }

    @Inject
    @ContributesBinding(AppGraph::class, ActiveTimerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> ActiveTimerScreenDecomposeComponentImpl
    ) : ActiveTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
