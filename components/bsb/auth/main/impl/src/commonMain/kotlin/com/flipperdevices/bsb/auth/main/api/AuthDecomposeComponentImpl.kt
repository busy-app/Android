package com.flipperdevices.bsb.auth.main.api

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.flipperdevices.bsb.auth.login.api.LoginDecomposeComponent
import com.flipperdevices.bsb.auth.main.model.AuthRootNavigationConfig
import com.flipperdevices.bsb.auth.signup.api.SignupDecomposeComponent
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.warn
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.findChildByConfig
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class AuthDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBackParameter: DecomposeOnBackParameter,
    @Assisted deeplink: Deeplink.Root.Auth?,
    private val mainScreenDecomposeComponent: (
        ComponentContext,
        StackNavigation<AuthRootNavigationConfig>,
        onComplete: () -> Unit,
        deeplink: Deeplink.Root.Auth?,
    ) -> MainScreenDecomposeComponentImpl,
    private val loginDecomposeComponentFactory: LoginDecomposeComponent.Factory,
    private val signupDecomposeComponentFactory: SignupDecomposeComponent.Factory
) : AuthDecomposeComponent<AuthRootNavigationConfig>(),
    ComponentContext by componentContext,
    LogTagProvider {
    override val TAG = "AuthDecomposeComponentImpl"

    override val stack = childStack(
        source = navigation,
        serializer = AuthRootNavigationConfig.serializer(),
        initialStack = {
            when (deeplink) {
                is Deeplink.Root.Auth.VerifyEmailLink.ResetPassword -> listOf(
                    AuthRootNavigationConfig.AuthRoot(null),
                    AuthRootNavigationConfig.LogIn(
                        email = deeplink.email,
                        preFilledPassword = null,
                        deeplink = deeplink
                    ),
                )

                is Deeplink.Root.Auth.VerifyEmailLink.SignUp -> listOf(
                    AuthRootNavigationConfig.AuthRoot(null),
                    AuthRootNavigationConfig.SignUp(
                        email = deeplink.email,
                        preFilledPassword = null,
                        deeplink = deeplink
                    ),
                )

                is Deeplink.Root.Auth.OAuth -> listOf(AuthRootNavigationConfig.AuthRoot(deeplink))
                null -> listOf(AuthRootNavigationConfig.AuthRoot(null))
            }
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: AuthRootNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        is AuthRootNavigationConfig.AuthRoot -> mainScreenDecomposeComponent(
            componentContext,
            navigation,
            onBackParameter::invoke,
            config.deeplink,
        )

        is AuthRootNavigationConfig.LogIn -> loginDecomposeComponentFactory(
            componentContext,
            onBack = navigation::pop,
            email = config.email,
            onComplete = onBackParameter::invoke,
            preFilledPassword = config.preFilledPassword,
            deeplink = config.deeplink
        )

        is AuthRootNavigationConfig.SignUp -> signupDecomposeComponentFactory(
            componentContext,
            onBack = navigation::pop,
            email = config.email,
            onComplete = onBackParameter::invoke,
            preFilledPassword = config.preFilledPassword,
            deeplink = config.deeplink
        )
    }

    @Composable
    override fun Render(modifier: Modifier) {
        super.Render(
            modifier
                .background(LocalPallet.current.surface.primary)
        )
    }

    override fun handleDeeplink(deeplink: Deeplink.Root.Auth) {
        val (config, componentClass) = when (deeplink) {
            is Deeplink.Root.Auth.OAuth -> AuthRootNavigationConfig.AuthRoot(
                deeplink = null
            ) to MainScreenDecomposeComponentImpl::class

            is Deeplink.Root.Auth.VerifyEmailLink.ResetPassword -> AuthRootNavigationConfig.LogIn(
                email = deeplink.email,
                preFilledPassword = null,
                deeplink = deeplink
            ) to LoginDecomposeComponent::class

            is Deeplink.Root.Auth.VerifyEmailLink.SignUp -> AuthRootNavigationConfig.SignUp(
                email = deeplink.email,
                preFilledPassword = null,
                deeplink = deeplink
            ) to SignupDecomposeComponent::class
        }

        val child = stack.findChildByConfig(config::class)
        val component = child?.instance
        if (component == null || !componentClass.isInstance(component)) {
            warn { "Can't find config $config, find only $child " }
            navigation.bringToFront(config)
        } else {
            navigation.bringToFront(child.configuration)
            when (deeplink) {
                is Deeplink.Root.Auth.OAuth ->
                    (component as MainScreenDecomposeComponentImpl).handleDeeplink(deeplink)

                is Deeplink.Root.Auth.VerifyEmailLink.ResetPassword ->
                    (component as LoginDecomposeComponent<*>).handleDeeplink(deeplink)

                is Deeplink.Root.Auth.VerifyEmailLink.SignUp ->
                    (component as SignupDecomposeComponent<*>).handleDeeplink(deeplink)
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, AuthDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter,
            deeplink: Deeplink.Root.Auth?
        ) -> AuthDecomposeComponentImpl
    ) : AuthDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter,
            deeplink: Deeplink.Root.Auth?
        ) = factory(componentContext, onBackParameter, deeplink)
    }
}
