package com.flipperdevices.bsb.timer.delayedstart.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsb.timer.delayedstart.composable.DelayedStartComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class DelayedStartScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    private val typeEndDelay: TypeEndDelay,
) : DelayedStartScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerApi.getState().collectAsState()
        when (val state = state) {


            ControlledTimerState.Finished -> Unit
            ControlledTimerState.NotStarted -> Unit
            is ControlledTimerState.Running -> {

                DelayedStartComposableContent(
                    typeEndDelay = typeEndDelay,
                    timerSettings = state.timerSettings,
                    currentIteration = state.currentIteration,
                    maxIteration = state.maxIterations,
                    onStartClick = {
                        TODO()
                    },
                    onFinishClick = {
                        timerApi.stop()
                    },
                )
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, DelayedStartScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            typeEndDelay: TypeEndDelay
        ) -> DelayedStartScreenDecomposeComponentImpl
    ) : DelayedStartScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            typeEndDelay: TypeEndDelay
        ) = factory(componentContext, typeEndDelay)
    }
}
