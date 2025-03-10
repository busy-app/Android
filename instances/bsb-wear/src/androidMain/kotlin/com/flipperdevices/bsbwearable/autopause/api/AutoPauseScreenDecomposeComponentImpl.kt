package com.flipperdevices.bsbwearable.autopause.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.util.confirmNextStep
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsbwearable.autopause.composable.AutoPauseScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AutoPauseScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
) : AutoPauseScreenDecomposeComponent(componentContext) {

    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return timerApi.getState()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        when (val timerState = timerState) {
            is ControlledTimerState.InProgress.Await -> {
                AutoPauseScreenComposable(
                    state = timerState,
                    onButtonClick = {
                        timerApi.confirmNextStep()
                    },
                    onStopClick = {
                        timerApi.stop()
                    },
                )
            }

            else -> Unit
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, AutoPauseScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> AutoPauseScreenDecomposeComponentImpl
    ) : AutoPauseScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
