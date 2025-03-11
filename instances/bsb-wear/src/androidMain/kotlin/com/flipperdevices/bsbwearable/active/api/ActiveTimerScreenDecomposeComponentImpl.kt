package com.flipperdevices.bsbwearable.active.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.util.pause
import com.flipperdevices.bsb.timer.background.util.resume
import com.flipperdevices.bsb.timer.background.util.skip
import com.flipperdevices.bsb.timer.focusdisplay.api.FocusDisplayDecomposeComponent
import com.flipperdevices.bsbwearable.active.composable.ActiveTimerScreenComposable
import com.flipperdevices.bsbwearable.interrupt.api.StopSessionDecomposeComponent
import com.flipperdevices.bsbwearable.interrupt.composable.PauseWearOverlayComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class ActiveTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    stopSessionDecomposeComponentFactory: StopSessionDecomposeComponent.Factory,
    private val timerApi: TimerApi,

    focusDisplayDecomposeComponentFactory: FocusDisplayDecomposeComponent.Factory,
) : ActiveTimerScreenDecomposeComponent(componentContext) {
    private val stopSessionDecomposeComponentFactory = stopSessionDecomposeComponentFactory.invoke(
        componentContext = childContext("atsdci_ssdcf")
    )

    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return timerApi.getState()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        ActiveTimerScreenComposable(
            timerState = timerState,
            onStopClick = {
                stopSessionDecomposeComponentFactory.show()
            },
            onSkipClick = {
                timerApi.skip()
            },
            onPauseClick = {
                timerApi.pause()
            }
        )

        if ((timerState as? ControlledTimerState.InProgress.Running)?.isOnPause == true) {
            PauseWearOverlayComposable(
                onResumeClick = {
                    timerApi.resume()
                }
            )
        }
        stopSessionDecomposeComponentFactory.Render(Modifier)
    }

    init {
        focusDisplayDecomposeComponentFactory.invoke(lifecycle = lifecycle)
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
