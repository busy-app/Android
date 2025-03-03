package com.flipperdevices.bsbwearable.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsbwearable.finish.composable.FinishScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class FinishScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : FinishScreenDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<ControlledTimerState> {
        return MutableStateFlow(
            ControlledTimerState.Finished
        ).asStateFlow()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerState by getTimerState().collectAsState()
        when (timerState) {
            is ControlledTimerState.Finished -> {
                FinishScreenComposable(
                    onReloadClick = {},
                    onButtonClick = {}
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
