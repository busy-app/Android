package com.flipperdevices.bsb.appblocker.screen.api

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.bsb.appblocker.screen.composable.AppBlockerScreenComposable
import com.flipperdevices.bsb.appblocker.screen.model.toInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted applicationInfo: ApplicationInfo,
    @Assisted private val onBackParameter: DecomposeOnBackParameter,
    statusBarIconStyleProvider: ThemeStatusBarIconStyleProvider,
    context: Context
) : AppBlockerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by statusBarIconStyleProvider {
    private val internalApplicationInfo = applicationInfo.toInternal(context)

    @Composable
    override fun Render(modifier: Modifier) {
        AppBlockerScreenComposable(
            modifier = modifier
                .fillMaxSize()
                .background(LocalPallet.current.surface.primary)
                .safeDrawingPadding(),
            applicationInfo = internalApplicationInfo,
            onBack = onBackParameter::invoke
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            applicationInfo: ApplicationInfo,
            onBackParameter: DecomposeOnBackParameter
        ) -> AppBlockerScreenDecomposeComponentImpl
    ) : AppBlockerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            applicationInfo: ApplicationInfo,
            onBackParameter: DecomposeOnBackParameter
        ) = factory(componentContext, applicationInfo, onBackParameter)
    }
}
