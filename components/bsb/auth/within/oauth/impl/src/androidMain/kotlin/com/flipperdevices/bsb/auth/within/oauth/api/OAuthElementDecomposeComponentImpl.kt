package com.flipperdevices.bsb.auth.within.oauth.api

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.common.composable.SignInWithButtonComposable
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.oauth.model.InternalOAuthProvider
import com.flipperdevices.bsb.auth.within.oauth.model.OAuthProvider
import com.flipperdevices.bsb.auth.within.oauth.model.toInternal
import com.flipperdevices.bsb.auth.within.oauth.viewmodel.OAuthElementViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class OAuthElementDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted oAuthProvider: OAuthProvider,
    @Assisted withInStateListener: SignWithInStateListener,
    private val viewModelFactory: (
        withInStateListener: SignWithInStateListener,
        oAuthProvider: InternalOAuthProvider
    ) -> OAuthElementViewModel
) : OAuthElementDecomposeComponent(componentContext) {
    private val provider = oAuthProvider.toInternal()

    private val viewModel = viewModelWithFactory(provider) {
        viewModelFactory(withInStateListener, provider)
    }

    @Composable
    override fun Render(modifier: Modifier, authState: SignWithInState) {
        val context = LocalContext.current
        SignInWithButtonComposable(
            modifier = modifier,
            icon = if (MaterialTheme.colors.isLight) {
                provider.iconId
            } else {
                provider.darkIconId
            },
            onClick = {
                viewModel.onStartAuth(context)
            },
            inProgress = authState is SignWithInState.InProgress &&
                authState.authWay == provider.authWay
        )
    }

    override fun onReceiveAuthToken(authCode: String) {
        viewModel.onAuthCodeReceive(authCode)
    }

    @Inject
    @ContributesBinding(AppGraph::class, OAuthElementDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            oAuthProvider: OAuthProvider,
            withInStateListener: SignWithInStateListener
        ) -> OAuthElementDecomposeComponentImpl
    ) : OAuthElementDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            oAuthProvider: OAuthProvider,
            withInStateListener: SignWithInStateListener,
        ) = factory(componentContext, oAuthProvider, withInStateListener)
    }
}
