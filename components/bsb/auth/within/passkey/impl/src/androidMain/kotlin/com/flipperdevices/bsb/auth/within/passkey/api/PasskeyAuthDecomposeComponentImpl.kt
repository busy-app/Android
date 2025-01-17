package com.flipperdevices.bsb.auth.within.passkey.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import busystatusbar.components.bsb.auth.within.passkey.impl.generated.resources.Res
import busystatusbar.components.bsb.auth.within.passkey.impl.generated.resources.material_ic_passkey
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.common.composable.SignInWithButtonComposable
import com.flipperdevices.bsb.auth.within.main.model.AuthWay
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.passkey.viewmodel.PasskeyViewModel
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory

class PasskeyAuthDecomposeComponentImpl(
    componentContext: ComponentContext,
    withInStateListener: SignWithInStateListener,
    passkeyViewModelFactory: PasskeyViewModel.Factory
) : PasskeyAuthDecomposeComponent(componentContext) {
    private val viewModel = viewModelWithFactory(withInStateListener) {
        passkeyViewModelFactory(withInStateListener)
    }


    @Composable
    override fun Render(
        modifier: Modifier,
        authState: SignWithInState
    ) {
        SignInWithButtonComposable(
            modifier = modifier,
            icon = Res.drawable.material_ic_passkey,
            onClick = {
                if (authState == SignWithInState.WaitingForInput) {
                    viewModel.onAuth()
                }
            },
            inProgress = authState is SignWithInState.InProgress &&
                    authState.authWay == AuthWay.GOOGLE
        )
    }
}