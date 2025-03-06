package com.flipperdevices.bsbwearable.root.api

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.di.WearMessengerModule
import com.flipperdevices.bsb.wear.messenger.model.TimerRequestUpdateMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsbwearable.active.api.ActiveTimerScreenDecomposeComponent
import com.flipperdevices.bsbwearable.autopause.api.AutoPauseScreenDecomposeComponent
import com.flipperdevices.bsbwearable.card.api.CardDecomposeComponent
import com.flipperdevices.bsbwearable.composable.SwipeToDismissBox
import com.flipperdevices.bsbwearable.finish.api.FinishScreenDecomposeComponent
import com.flipperdevices.bsbwearable.root.api.model.RootNavigationConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class RootDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val wearMessengerModule: WearMessengerModule,
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
        wearMessengerModule
            .wearMessageConsumer
            .messagesFlow
            .onEach { Log.d("RootDecomposeComponent", ": $it") }
            .filter {
                println("Got some $it")
                it.path == TimerTimestampMessage.path
            }
            .map { it.value as? TimerTimestamp }
            .onEach { timerApi.setTimestampState(it) }
            .launchIn(coroutineScope())
        doOnResume {
            coroutineScope().launch {
                wearMessengerModule
                    .wearMessageProducer
                    .produce(TimerRequestUpdateMessage, 0)
            }
        }
        timerApi
            .getState()
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
            }.distinctUntilChanged()
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
