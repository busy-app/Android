package com.flipperdevices.bsb.timer.main.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.active.api.ActiveTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.cards.api.CardsDecomposeComponent
import com.flipperdevices.bsb.timer.done.api.DoneTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.finish.api.RestTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.main.model.TimerMainNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class TimerMainDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val activeTimerScreenDecomposeComponentFactory: ActiveTimerScreenDecomposeComponent.Factory,
    private val cardsDecomposeComponentFactory: CardsDecomposeComponent.Factory,
    private val restTimerScreenDecomposeComponentFactory: RestTimerScreenDecomposeComponent.Factory,
    private val doneTimerScreenDecomposeComponentFactory: DoneTimerScreenDecomposeComponent.Factory,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    timerApi: TimerApi,
) : TimerMainDecomposeComponent<TimerMainNavigationConfig>(),
    StatusBarIconStyleProvider by iconStyleProvider,
    ComponentContext by componentContext {

    private fun ControlledTimerState.getScreen(): TimerMainNavigationConfig {
        val screen = when (this) {
            ControlledTimerState.Finished -> TimerMainNavigationConfig.Finished
            ControlledTimerState.NotStarted -> TimerMainNavigationConfig.Main
            is ControlledTimerState.Running.LongRest -> TimerMainNavigationConfig.LongRest
            is ControlledTimerState.Running.Rest -> TimerMainNavigationConfig.Rest
            is ControlledTimerState.Running.Work -> TimerMainNavigationConfig.Work
        }
        return screen
    }

    init {
        @Suppress("MagicNumber")
        timerApi.getState()
            .distinctUntilChangedBy { state ->
                when (state) {
                    ControlledTimerState.Finished -> 0
                    ControlledTimerState.NotStarted -> 1
                    is ControlledTimerState.Running.LongRest -> 2
                    is ControlledTimerState.Running.Rest -> 3
                    is ControlledTimerState.Running.Work -> 4
                }
            }
            .onEach { state -> navigation.replaceAll(state.getScreen()) }
            .launchIn(coroutineScope())
    }

    override val stack = childStack(
        source = navigation,
        serializer = TimerMainNavigationConfig.serializer(),
        initialConfiguration = timerApi.getState().value.getScreen(),
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: TimerMainNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        TimerMainNavigationConfig.Main -> cardsDecomposeComponentFactory(
            componentContext = componentContext,
        )

        TimerMainNavigationConfig.Work -> activeTimerScreenDecomposeComponentFactory(componentContext)
        TimerMainNavigationConfig.Finished -> doneTimerScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext,
            onFinishCallback = {
                navigation.replaceAll(TimerMainNavigationConfig.Main)
            }
        )

        TimerMainNavigationConfig.LongRest -> restTimerScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext,
            breakType = RestTimerScreenDecomposeComponent.BreakType.LONG
        )

        TimerMainNavigationConfig.Rest -> restTimerScreenDecomposeComponentFactory.invoke(
            componentContext = componentContext,
            breakType = RestTimerScreenDecomposeComponent.BreakType.SHORT
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerMainDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> TimerMainDecomposeComponentImpl
    ) : TimerMainDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
