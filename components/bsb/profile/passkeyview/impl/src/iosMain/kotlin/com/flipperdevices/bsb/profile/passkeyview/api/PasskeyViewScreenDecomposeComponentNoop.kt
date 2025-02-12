package com.flipperdevices.bsb.profile.passkeyview.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class PasskeyViewScreenDecomposeComponentNoop(
    @Assisted componentContext: ComponentContext,
) : PasskeyViewScreenDecomposeComponent(componentContext) {
    @Composable
    override fun Render(modifier: Modifier) {
        // Empty
    }

    @Inject
    @ContributesBinding(AppGraph::class, PasskeyViewScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> PasskeyViewScreenDecomposeComponentNoop
    ) : PasskeyViewScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
