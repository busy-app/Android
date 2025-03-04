package com.flipperdevices.bsbwearable.active.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsbwearable.active.composable.ActiveTimerScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration.Companion.seconds

@Inject
class ActiveTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : ActiveTimerScreenDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return MutableStateFlow(
            ControlledTimerState.InProgress.Running.Work(
                timeLeft = 110.seconds,
                isOnPause = false,
                timerSettings = TimerSettings(),
                currentIteration = 0,
                maxIterations = 1
            )
        ).asStateFlow()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        ActiveTimerScreenComposable(
            timerState = timerState,
            onStopClick = {},
            onSkipClick = {},
            onPauseClick = {}
        )
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
