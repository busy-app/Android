package com.flipperdevices.bsbwearable.active.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.bsbwearable.active.composable.ActiveTimerScreenComposable
import com.flipperdevices.bsbwearable.interrupt.api.StopSessionDecomposeComponent
import com.flipperdevices.bsbwearable.interrupt.composable.PauseWearOverlayComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class ActiveTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    stopSessionDecomposeComponentFactory: StopSessionDecomposeComponent.Factory,
    private val timerApi: TimerApi,
    private val wearMessageProducer: WearMessageProducer
) : ActiveTimerScreenDecomposeComponent(componentContext) {
    private val stopSessionDecomposeComponentFactory = stopSessionDecomposeComponentFactory.invoke(
        componentContext = childContext("atsdci_ssdcf")
    )

    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return timerApi.getState()
    }

    private val scope = coroutineScope()

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        ActiveTimerScreenComposable(
            timerState = timerState,
            onStopClick = {
                stopSessionDecomposeComponentFactory.show()
            },
            onSkipClick = {
                scope.launch { wearMessageProducer.produce(TimerActionMessage.Skip) }
            },
            onPauseClick = {
                scope.launch { wearMessageProducer.produce(TimerActionMessage.Pause) }
            }
        )

        if ((timerState as? ControlledTimerState.InProgress.Running)?.isOnPause == true) {
            PauseWearOverlayComposable(
                onResumeClick = {
                    scope.launch { wearMessageProducer.produce(TimerActionMessage.Resume) }
                }
            )
        }
        stopSessionDecomposeComponentFactory.Render(Modifier)
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
