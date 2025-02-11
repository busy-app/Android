package com.flipperdevices.bsb.auth.signup.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.flipperdevices.bsb.auth.confirmpassword.api.AuthConfirmPasswordScreenDecomposeComponent
import com.flipperdevices.bsb.auth.confirmpassword.model.ConfirmPasswordType
import com.flipperdevices.bsb.auth.otp.screen.api.AuthOtpScreenDecomposeComponent
import com.flipperdevices.bsb.auth.otp.screen.model.AuthOtpType
import com.flipperdevices.bsb.auth.signup.model.SignupNavigationConfig
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.findChildByConfig
import com.flipperdevices.ui.decompose.popOr
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class SignupDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: DecomposeOnBackParameter,
    @Assisted private val email: String,
    @Assisted private val preFilledPassword: String?,
    @Assisted private val onComplete: () -> Unit,
    @Assisted deeplink: Deeplink.Root.Auth.VerifyEmailLink.SignUp?,
    private val signupScreenDecomposeComponent: (
        componentContext: ComponentContext,
        onBack: DecomposeOnBackParameter,
        email: String,
        onContinue: () -> Unit
    ) -> SignupScreenDecomposeComponentImpl,
    private val otpScreenDecomposeComponent: AuthOtpScreenDecomposeComponent.Factory,
    private val confirmPasswordScreenDecomposeComponent: AuthConfirmPasswordScreenDecomposeComponent.Factory
) : SignupDecomposeComponent<SignupNavigationConfig>(),
    ComponentContext by componentContext {
    override val stack = childStack(
        source = navigation,
        serializer = SignupNavigationConfig.serializer(),
        initialStack = {
            if (deeplink != null) {
                listOf(
                    SignupNavigationConfig.Main,
                    SignupNavigationConfig.ConfirmEmail(deeplink)
                )
            } else {
                listOf(SignupNavigationConfig.Main)
            }
        },
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: SignupNavigationConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        SignupNavigationConfig.Main -> signupScreenDecomposeComponent(
            componentContext,
            { navigation.popOr(onBack::invoke) },
            email,
            { navigation.pushToFront(SignupNavigationConfig.ConfirmEmail(null)) },
        )

        is SignupNavigationConfig.ConfirmEmail -> otpScreenDecomposeComponent(
            componentContext = componentContext,
            onBack = { navigation.popOr(onBack::invoke) },
            otpType = AuthOtpType.VerifyEmail(
                email = email
            ),
            onOtpComplete = { otpCode ->
                navigation.pushToFront(SignupNavigationConfig.EnterPassword(code = otpCode))
            },
            deeplink = config.deeplink
        )

        is SignupNavigationConfig.EnterPassword -> confirmPasswordScreenDecomposeComponent(
            componentContext = componentContext,
            type = ConfirmPasswordType.SignUpPassword(
                email = email,
                otpCode = config.code,
                preFilledPassword = preFilledPassword
            ),
            onBackParameter = { navigation.popOr(onBack::invoke) },
            onComplete = onComplete
        )
    }

    override fun handleDeeplink(deeplink: Deeplink.Root.Auth.VerifyEmailLink.SignUp) {
        val child = stack.findChildByConfig(SignupNavigationConfig.ConfirmEmail::class)
        val component = child?.instance
        if (component != null && component is AuthOtpScreenDecomposeComponent) {
            component.handleDeeplink(deeplink)
            navigation.pushToFront(child.configuration)
        } else {
            navigation.pushToFront(SignupNavigationConfig.ConfirmEmail(deeplink))
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, SignupDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            email: String,
            preFilledPassword: String?,
            onComplete: () -> Unit,
            deeplink: Deeplink.Root.Auth.VerifyEmailLink.SignUp?
        ) -> SignupDecomposeComponentImpl
    ) : SignupDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBack: DecomposeOnBackParameter,
            email: String,
            preFilledPassword: String?,
            onComplete: () -> Unit,
            deeplink: Deeplink.Root.Auth.VerifyEmailLink.SignUp?
        ) = factory(componentContext, onBack, email, preFilledPassword, onComplete, deeplink)
    }
}
