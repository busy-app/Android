package com.flipperdevices.bsb.auth.within.main.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class SignWithInMainDecomposeComponentNoop(
    @Assisted componentContext: ComponentContext,
) : SignWithInMainDecomposeComponent(componentContext),
    ComponentContext by componentContext {
    override fun handleDeeplink(deeplink: Deeplink.Root.Auth.OAuth) = Unit

    @Composable
    override fun Render(modifier: Modifier, authState: SignWithInState) = Unit

    @Inject
    @ContributesBinding(AppGraph::class, SignWithInMainDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> SignWithInMainDecomposeComponentNoop
    ) : SignWithInMainDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            withInStateListener: SignWithInStateListener,
            deeplink: Deeplink.Root.Auth.OAuth?,
        ) = factory(componentContext)
    }
}
