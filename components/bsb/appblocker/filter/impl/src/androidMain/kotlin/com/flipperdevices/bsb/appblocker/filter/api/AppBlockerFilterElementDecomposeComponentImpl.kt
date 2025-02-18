package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.appblocker.filter.composable.card.EmptyListAppsBoxComposable
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerFilterElementDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    filterScreenDecomposeComponentFactory: (ComponentContext) -> AppBlockerFilterScreenDecomposeComponent
) : AppBlockerFilterElementDecomposeComponent(componentContext) {
    private val filterScreenDecomposeComponent = filterScreenDecomposeComponentFactory(
        childContext("appBlockerFilter_screen")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        EmptyListAppsBoxComposable(
            modifier = modifier,
            onClick = filterScreenDecomposeComponent::show
        )
        filterScreenDecomposeComponent.Render(Modifier)
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerFilterElementDecomposeComponent.Factory::class)
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