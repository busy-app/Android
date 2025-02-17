package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerFilterScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted onBackParameter: DecomposeOnBackParameter
) : AppBlockerFilterScreenDecomposeComponent(componentContext) {
    @Composable
    override fun Render(modifier: Modifier) {

    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerFilterScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) -> AppBlockerFilterScreenDecomposeComponentImpl
    ) : AppBlockerFilterScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) = factory(componentContext, onBackParameter)
    }
}