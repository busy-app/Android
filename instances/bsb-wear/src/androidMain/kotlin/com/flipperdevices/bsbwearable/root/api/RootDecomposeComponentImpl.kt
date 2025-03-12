package com.flipperdevices.bsbwearable.root.api

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsbwearable.active.api.ActiveTimerScreenDecomposeComponent
import com.flipperdevices.bsbwearable.autopause.api.AutoPauseScreenDecomposeComponent
import com.flipperdevices.bsbwearable.card.api.CardDecomposeComponent
import com.flipperdevices.bsbwearable.composable.SwipeToDismissBox
import com.flipperdevices.bsbwearable.finish.api.FinishScreenDecomposeComponent
import com.flipperdevices.bsbwearable.root.api.model.RootNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class RootDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val activeTimerScreenDecomposeComponentFactory: ActiveTimerScreenDecomposeComponent.Factory,
    private val autoPauseScreenDecomposeComponentFactory: AutoPauseScreenDecomposeComponent.Factory,
    private val finishScreenDecomposeComponentFactory: FinishScreenDecomposeComponent.Factory,
    private val cardDecomposeComponentFactory: CardDecomposeComponent.Factory,
    private val timerApi: TimerApi
) : RootDecomposeComponent(),
    ComponentContext by componentContext {
    override val stack: Value<ChildStack<RootNavigationConfig, DecomposeComponent>> = childStack(
        source = navigation,
        serializer = RootNavigationConfig.serializer(),
        initialStack = {
            listOf(RootNavigationConfig.Card)
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    init {
        @Suppress("MagicNumber")
        timerApi
            .getState()
            .distinctUntilChangedBy { state ->
                when (state) {
                    ControlledTimerState.Finished -> 0
                    ControlledTimerState.NotStarted -> 1
                    is ControlledTimerState.InProgress.Running.LongRest -> 2
                    is ControlledTimerState.InProgress.Running.Rest -> 3
                    is ControlledTimerState.InProgress.Running.Work -> 4
                    is ControlledTimerState.InProgress.Await -> 5
                }
            }
            .map {
                when (it) {
                    ControlledTimerState.Finished -> {
                        RootNavigationConfig.Finish
                    }

                    is ControlledTimerState.InProgress.Await -> {
                        RootNavigationConfig.AutoPause
                    }

                    is ControlledTimerState.InProgress.Running.LongRest -> {
                        RootNavigationConfig.Active
                    }

                    is ControlledTimerState.InProgress.Running.Rest -> {
                        RootNavigationConfig.Active
                    }

                    is ControlledTimerState.InProgress.Running.Work -> {
                        RootNavigationConfig.Active
                    }

                    ControlledTimerState.NotStarted -> {
                        RootNavigationConfig.Card
                    }
                }
            }
            .onEach { navigation.replaceAll(it) }
            .launchIn(coroutineScope())
    }

    private fun child(
        config: RootNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        RootNavigationConfig.Active -> activeTimerScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext
        )

        RootNavigationConfig.AutoPause -> autoPauseScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext
        )

        RootNavigationConfig.Finish -> finishScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext
        )

        RootNavigationConfig.Card -> cardDecomposeComponentFactory.invoke(
            componentContext = componentContext
        )
    }

    @Composable
    override fun Render(modifier: Modifier) {
        SwipeToDismissBox(
            stack = stack,
            modifier = Modifier.fillMaxSize(),
            onDismiss = {},
            content = { child -> child.instance.Render(Modifier) }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, RootDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
        ) -> RootDecomposeComponentImpl
    ) : RootDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ) = factory(componentContext)
    }
}
