package com.flipperdevices.bsb.timer.finish.api

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
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.util.pause
import com.flipperdevices.bsb.timer.background.util.resume
import com.flipperdevices.bsb.timer.background.util.skip
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsb.timer.common.composable.appbar.PauseFullScreenOverlayComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.stop.StopSessionSheetDecomposeComponentImpl
import com.flipperdevices.bsb.timer.finish.composable.TimerRestComposableScreen
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
class RestTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val breakType: BreakType,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    focusDisplayDecomposeComponentFactory: FocusDisplayDecomposeComponent.Factory,
    stopSessionSheetDecomposeComponentFactory: (
        componentContext: ComponentContext,
        onConfirm: () -> Unit
    ) -> StopSessionSheetDecomposeComponentImpl,
    private val metricApi: MetricApi
) : RestTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    init {
        focusDisplayDecomposeComponentFactory.invoke(lifecycle = lifecycle)
    }

    private val stopSessionSheetDecomposeComponent = stopSessionSheetDecomposeComponentFactory.invoke(
        childContext("rest_stopSessionSheetDecomposeComponent"),
        {
            timerApi.getTimestampState().value
                .runningOrNull
                ?.let { running -> Clock.System.now().minus(running.noOffsetStart) }
                ?.let { timePassed ->
                    metricApi.reportEvent(BEvent.TimerAborted(timePassed.inWholeMilliseconds))
                }

            timerApi.stop()
        }
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerApi.getState().collectAsState()
        val hazeState = remember { HazeState() }

        when (val state = state) {
            is ControlledTimerState.InProgress.Running -> {
                TimerRestComposableScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .hazeSource(hazeState),
                    onSkip = {
                        timerApi.getTimestampState().value
                            .runningOrNull
                            ?.let { running -> Clock.System.now().minus(running.noOffsetStart) }
                            ?.let { timePassed ->
                                metricApi.reportEvent(BEvent.TimerSkipped(timePassed.inWholeMilliseconds))
                            }

                        timerApi.skip()
                    },
                    state = state,
                    statusType = when (breakType) {
                        BreakType.SHORT -> StatusType.REST
                        BreakType.LONG -> StatusType.LONG_REST
                    },
                    onBackClick = {
                        stopSessionSheetDecomposeComponent.show()
                    },
                    onPauseClick = {
                        timerApi.getTimestampState().value
                            .runningOrNull
                            ?.let { running -> running.pause?.minus(running.noOffsetStart) }
                            ?.let { timePassed ->
                                metricApi.reportEvent(BEvent.TimerPaused(timePassed.inWholeMilliseconds))
                            }

                        timerApi.pause()
                    }
                )
                if (state.isOnPause) {
                    PauseFullScreenOverlayComposable(
                        onStartClick = {
                            timerApi.getTimestampState().value
                                .runningOrNull
                                ?.let { running -> running.pause?.minus(Clock.System.now())?.absoluteValue }
                                ?.let { timePausedPassed ->
                                    metricApi.reportEvent(BEvent.TimerResumed(timePausedPassed.inWholeMilliseconds))
                                }

                            timerApi.resume()
                        }
                    )
                }
            }

            is ControlledTimerState.InProgress.Await,
            ControlledTimerState.NotStarted,
            ControlledTimerState.Finished -> Unit
        }
        stopSessionSheetDecomposeComponent.Render(hazeState)
    }

    @Inject
    @ContributesBinding(AppGraph::class, RestTimerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            breakType: BreakType
        ) -> RestTimerScreenDecomposeComponentImpl
    ) : RestTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            breakType: BreakType
        ) = factory(componentContext, breakType)
    }
}
