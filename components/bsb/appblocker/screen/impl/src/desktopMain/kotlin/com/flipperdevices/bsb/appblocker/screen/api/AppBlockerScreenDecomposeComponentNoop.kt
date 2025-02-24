package com.flipperdevices.bsb.appblocker.screen.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerScreenDecomposeComponentNoop(
    @Assisted componentContext: ComponentContext,
    @Assisted applicationInfo: ApplicationInfo,
    @Assisted onBackParameter: DecomposeOnBackParameter,
) : AppBlockerScreenDecomposeComponent(componentContext) {
    @Composable
    override fun Render(modifier: Modifier) {
        // Empty
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            applicationInfo: ApplicationInfo,
            onBackParameter: DecomposeOnBackParameter
        ) -> AppBlockerScreenDecomposeComponentNoop
    ) : AppBlockerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            applicationInfo: ApplicationInfo,
            onBackParameter: DecomposeOnBackParameter
        ) = factory(componentContext, applicationInfo, onBackParameter)
    }
}
