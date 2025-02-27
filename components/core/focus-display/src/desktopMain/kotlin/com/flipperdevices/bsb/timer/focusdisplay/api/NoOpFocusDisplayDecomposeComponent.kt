package com.flipperdevices.bsb.timer.focusdisplay.api

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class NoOpFocusDisplayDecomposeComponent(
    @Assisted lifecycle: Lifecycle
) : FocusDisplayDecomposeComponent(lifecycle) {

    @Inject
    @ContributesBinding(AppGraph::class, FocusDisplayDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            lifecycle: Lifecycle
        ) -> NoOpFocusDisplayDecomposeComponent
    ) : FocusDisplayDecomposeComponent.Factory {
        override fun invoke(
            lifecycle: Lifecycle
        ) = factory(lifecycle)
    }
}
