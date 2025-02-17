package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerFilterElementDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : AppBlockerFilterElementDecomposeComponent(componentContext) {
    @Composable
    override fun Render(modifier: Modifier) {

    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerFilterScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> AppBlockerFilterElementDecomposeComponentImpl
    ) : AppBlockerFilterElementDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}