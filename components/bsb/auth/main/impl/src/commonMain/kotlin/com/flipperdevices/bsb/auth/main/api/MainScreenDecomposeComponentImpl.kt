package com.flipperdevices.bsb.auth.main.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.flipperdevices.bsb.auth.main.composable.AuthMainComposableScreen
import com.flipperdevices.bsb.auth.main.model.AuthMainState
import com.flipperdevices.bsb.auth.main.model.AuthRootNavigationConfig
import com.flipperdevices.bsb.auth.main.viewmodel.AuthMainViewModel
import com.flipperdevices.bsb.auth.within.main.api.SignWithInMainDecomposeComponent
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@Suppress("LongParameterList")
class MainScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted authNavigation: StackNavigation<AuthRootNavigationConfig>,
    @Assisted onComplete: () -> Unit,
    @Assisted deeplink: Deeplink.Root.Auth?,
    authMainViewModel: (
        StackNavigation<AuthRootNavigationConfig>,
        onComplete: () -> Unit
    ) -> AuthMainViewModel,
    signWithInMainDecomposeComponent: SignWithInMainDecomposeComponent.Factory
) : ScreenDecomposeComponent(componentContext) {
    private val authViewModel = viewModelWithFactory(authNavigation to onComplete) {
        authMainViewModel(authNavigation, onComplete)
    }
    private val signWithInDecomposeComponent = signWithInMainDecomposeComponent(
        componentContext = childContext("signWithIn_main"),
        withInStateListener = authViewModel,
        deeplink = deeplink as? Deeplink.Root.Auth.OAuth
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by authViewModel.getState().collectAsState()
        AuthMainComposableScreen(
            state,
            onLogin = authViewModel::onLogin,
            onPrefillPassword = authViewModel::onPrefillPassword,
            signInWith = { signInWithModifier ->
                signWithInDecomposeComponent.Render(
                    signInWithModifier,
                    state.toSignWithInState()
                )
            }
        )
    }

    fun handleDeeplink(deeplink: Deeplink.Root.Auth.OAuth) {
        signWithInDecomposeComponent.handleDeeplink(deeplink)
    }
}

fun AuthMainState.toSignWithInState() = when (this) {
    is AuthMainState.AuthInProgress -> SignWithInState.InProgress(authWay)
    AuthMainState.WaitingForInput -> SignWithInState.WaitingForInput
}
