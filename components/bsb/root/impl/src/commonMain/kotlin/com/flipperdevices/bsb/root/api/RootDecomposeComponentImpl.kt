package com.flipperdevices.bsb.root.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.bsb.profile.main.api.ProfileDecomposeComponent
import com.flipperdevices.bsb.root.deeplink.RootDeeplinkHandlerImpl
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import com.flipperdevices.bsb.timer.main.api.TimerMainDecomposeComponent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.warn
import com.flipperdevices.inappnotification.api.InAppNotificationDecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.findComponentByConfig
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class RootDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted initialDeeplink: Deeplink?,
    private val profileDecomposeComponentFactory: ProfileDecomposeComponent.Factory,
    private val timerMainDecomposeComponentFactory: TimerMainDecomposeComponent.Factory,
    inAppNotificationFactory: InAppNotificationDecomposeComponent.Factory
) : RootDecomposeComponent(),
    ComponentContext by componentContext {
    private val inAppNotificationDecomposeComponent = inAppNotificationFactory(
        componentContext = childContext("inAppNotification")
    )

    override val stack = childStack(
        source = navigation,
        serializer = RootNavigationConfig.serializer(),
        initialStack = {
            if (initialDeeplink != null) {
                listOf(
                    RootNavigationConfig.Timer,
                    RootDeeplinkHandlerImpl.getConfigFromDeeplink(initialDeeplink)
                )
            } else {
                listOf(RootNavigationConfig.Timer)
            }
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private val deeplinkHandler = RootDeeplinkHandlerImpl(navigation)

    private fun child(
        config: RootNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        is RootNavigationConfig.Profile -> profileDecomposeComponentFactory(
            componentContext,
            navigation::pop,
            config.deeplink
        )

        RootNavigationConfig.Timer -> timerMainDecomposeComponentFactory(
            componentContext
        )
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val childStack by stack.subscribeAsState()

        CompositionLocalProvider(
            LocalRootNavigation provides this
        ) {
            Box(
                modifier
                    .fillMaxSize()
            ) {
                Children(
                    modifier = Modifier.fillMaxSize(),
                    stack = childStack,
                ) {
                    it.instance.Render(Modifier)
                }

                inAppNotificationDecomposeComponent.Render(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .systemBarsPadding()
                )
            }
        }
    }

    override fun push(config: RootNavigationConfig) {
        navigation.pushToFront(config)
    }

    override fun handleDeeplink(deeplink: Deeplink) {
        when (deeplink) {
            is Deeplink.Root.Auth -> {
                val component = stack.findComponentByConfig(RootNavigationConfig.Profile::class)
                if (component == null || component !is ProfileDecomposeComponent<*>) {
                    warn { "Bottom bar component is not exist in stack, but first pair screen already passed" }
                    navigation.bringToFront(RootNavigationConfig.Profile(deeplink))
                } else {
                    component.handleDeeplink(deeplink)
                }
            }

            else -> deeplinkHandler.handleDeeplink(deeplink)
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, RootDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            initialDeeplink: Deeplink?
        ) -> RootDecomposeComponentImpl
    ) : RootDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            initialDeeplink: Deeplink?
        ) = factory(componentContext, initialDeeplink)
    }
}
