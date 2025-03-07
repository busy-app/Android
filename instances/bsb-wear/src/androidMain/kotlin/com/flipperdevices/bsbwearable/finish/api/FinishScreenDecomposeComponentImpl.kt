package com.flipperdevices.bsbwearable.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.bsbwearable.finish.composable.FinishScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class FinishScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
    private val wearMessageProducer: WearMessageProducer
) : FinishScreenDecomposeComponent(componentContext) {

    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return timerApi.getState()
    }

    private val scope = coroutineScope()

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        when (timerState) {
            is ControlledTimerState.Finished -> {
                FinishScreenComposable(
                    onReloadClick = {
                        scope.launch { wearMessageProducer.produce(TimerActionMessage.Restart) }
                    },
                    onButtonClick = {
                        scope.launch { wearMessageProducer.produce(TimerActionMessage.Finish) }
                    }
                )
            }

            else -> Unit
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, FinishScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> FinishScreenDecomposeComponentImpl
    ) : FinishScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
