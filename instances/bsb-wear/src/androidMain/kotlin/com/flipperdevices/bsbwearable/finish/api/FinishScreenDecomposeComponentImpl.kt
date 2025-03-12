package com.flipperdevices.bsbwearable.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsbwearable.finish.composable.FinishScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class FinishScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
) : FinishScreenDecomposeComponent(componentContext) {

    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return timerApi.getState()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        when (timerState) {
            is ControlledTimerState.Finished -> {
                FinishScreenComposable(
                    onReloadClick = onReloadClick@{
                        val runningState = timerApi.getTimestampState().value as? TimerTimestamp.Running
                        runningState ?: return@onReloadClick
                        timerApi.startWith(runningState.settings)
                    },
                    onButtonClick = {
                        timerApi.stop()
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
