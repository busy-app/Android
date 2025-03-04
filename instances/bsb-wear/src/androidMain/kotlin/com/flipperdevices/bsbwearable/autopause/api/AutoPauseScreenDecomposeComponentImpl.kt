package com.flipperdevices.bsbwearable.autopause.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsbwearable.autopause.composable.AutoPauseScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AutoPauseScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : AutoPauseScreenDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return MutableStateFlow(
            ControlledTimerState.InProgress.Await(
                timerSettings = TimerSettings(),
                currentIteration = 0,
                maxIterations = 1,
                pausedAt = Instant.DISTANT_PAST,
                type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
            )
        ).asStateFlow()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        when (val timerState = timerState) {
            is ControlledTimerState.InProgress.Await -> {
                AutoPauseScreenComposable(
                    state = timerState,
                    onButtonClick = {},
                    onStopClick = {},
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
