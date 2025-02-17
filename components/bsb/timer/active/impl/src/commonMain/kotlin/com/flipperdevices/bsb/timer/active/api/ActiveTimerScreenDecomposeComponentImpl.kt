package com.flipperdevices.bsb.timer.active.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.active.composable.TimerOnComposableScreen
import com.flipperdevices.bsb.timer.background.api.TimerService
import com.flipperdevices.bsb.timer.background.model.TimerServiceState
import com.flipperdevices.bsb.timer.common.composable.appbar.PauseFullScreenOverlayComposable
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class ActiveTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerService: TimerService,
    private val stopSessionSheetDecomposeComponentFactory: StopSessionSheetDecomposeComponent.Factory
) : ActiveTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    private val stopSessionSheetDecomposeComponent = stopSessionSheetDecomposeComponentFactory.invoke(
        childContext("stopSessionSheetDecomposeComponent"),
        onConfirm = {
            timerService.stop()
        }
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerService.state.collectAsState()
        when (val state = state) {
            TimerServiceState.Finished -> Unit
            TimerServiceState.Pending -> Unit
            is TimerServiceState.Started -> {
                val timerState = state.timerState
                TimerOnComposableScreen(
                    modifier = modifier,
                    workPhaseText = when {
                        !state.timerSettings.intervalsSettings.isEnabled -> null
                        else -> {
                            "${state.currentUiIteration}/${state.maxUiIteration}"
                        }
                    },
                    timeLeft = timerState.timerState.duration,
                    onSkip = {
                        timerService.skip()
                    },
                    onPauseClick = {
                        timerService.togglePause()
                    },
                    onBack = {
                        stopSessionSheetDecomposeComponent.show()
                    }
                )
                if (timerState.isOnPause) {
                    PauseFullScreenOverlayComposable(
                        onStartClick = { timerService.togglePause() }
                    )
                }
            }
        }

        stopSessionSheetDecomposeComponent.Render(Modifier)
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
