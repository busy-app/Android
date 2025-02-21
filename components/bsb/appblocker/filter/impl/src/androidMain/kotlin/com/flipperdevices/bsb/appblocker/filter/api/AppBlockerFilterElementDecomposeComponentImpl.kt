package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.appblocker.filter.composable.card.EmptyListAppsBoxComposable
import com.flipperdevices.bsb.appblocker.filter.composable.card.FilledListAppsBoxComposable
import com.flipperdevices.bsb.appblocker.filter.model.card.AppBlockerCardListState
import com.flipperdevices.bsb.appblocker.filter.viewmodel.card.AppBlockerCardListViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerFilterElementDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    filterScreenDecomposeComponentFactory: (ComponentContext) -> AppBlockerFilterScreenDecomposeComponent,
    viewModelFactory: () -> AppBlockerCardListViewModel
) : AppBlockerFilterElementDecomposeComponent(componentContext) {
    private val filterScreenDecomposeComponent = filterScreenDecomposeComponentFactory(
        childContext("appBlockerFilter_screen")
    )

    private val viewModel by lazy {
        viewModelWithFactory(null) {
            viewModelFactory()
        }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val state by viewModel.getState().collectAsState()
        when (val localState = state) {
            AppBlockerCardListState.Empty -> EmptyListAppsBoxComposable(
                modifier = modifier,
                onClick = filterScreenDecomposeComponent::show
            )

            is AppBlockerCardListState.Loaded -> {
                FilledListAppsBoxComposable(
                    modifier = modifier,
                    isAllBlocked = localState.isAllBlocked,
                    items = localState.icons,
                    onClick = filterScreenDecomposeComponent::show
                )
            }
        }
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
