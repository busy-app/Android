package com.flipperdevices.bsb.timer.main.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.timer.active.api.ActiveTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.background.api.TimerService
import com.flipperdevices.bsb.timer.background.model.TimerServiceState
import com.flipperdevices.bsb.timer.cards.api.CardsDecomposeComponent
import com.flipperdevices.bsb.timer.done.api.DoneTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.finish.api.RestTimerScreenDecomposeComponent
import com.flipperdevices.bsb.timer.main.model.TimerMainNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class TimerMainDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val activeTimerScreenDecomposeComponentFactory: ActiveTimerScreenDecomposeComponent.Factory,
    private val cardsDecomposeComponentFactory: CardsDecomposeComponent.Factory,
    private val restTimerScreenDecomposeComponentFactory: RestTimerScreenDecomposeComponent.Factory,
    private val doneTimerScreenDecomposeComponentFactory: DoneTimerScreenDecomposeComponent.Factory,
    timerService: TimerService,
) : TimerMainDecomposeComponent<TimerMainNavigationConfig>(),
    ComponentContext by componentContext {

    private fun TimerServiceState.getScreen(): TimerMainNavigationConfig {
        val screen = when (this) {
            TimerServiceState.Finished -> TimerMainNavigationConfig.Finished
            TimerServiceState.Pending -> TimerMainNavigationConfig.Main
            is TimerServiceState.Started -> {
                when (this.status) {
                    TimerServiceState.Status.WORK -> TimerMainNavigationConfig.Work
                    TimerServiceState.Status.REST -> TimerMainNavigationConfig.Rest
                    TimerServiceState.Status.LONG_REST -> TimerMainNavigationConfig.LongRest
                }
            }
        }
        return screen
    }

    init {
        timerService.state
            .distinctUntilChangedBy { state ->
                when (state) {
                    TimerServiceState.Finished -> state::class
                    TimerServiceState.Pending -> state::class
                    is TimerServiceState.Started -> {
                        state::class.toString() + state.status
                    }
                }
            }
            .onEach { state ->
                navigation.replaceAll(state.getScreen())
            }.launchIn(coroutineScope())
    }

    override val stack = childStack(
        source = navigation,
        serializer = TimerMainNavigationConfig.serializer(),
        initialConfiguration = timerService.state.value.getScreen(),
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
