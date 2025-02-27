package com.flipperdevices.bsb.appblocker.card.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.appblocker.card.composable.AppBlockerHeaderComposable
import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterElementDecomposeComponent
import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionBlockDecomposeComponent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerCardContentDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBackParameter: DecomposeOnBackParameter,
    private val appBlockerApi: AppBlockerApi,
    appBlockerPermissionBlockFactory: AppBlockerPermissionBlockDecomposeComponent.Factory,
    appBlockerFilterBlockFactory: AppBlockerFilterElementDecomposeComponent.Factory
) : AppBlockerCardContentDecomposeComponent(componentContext) {
    private val appBlockerPermissionCardContent = appBlockerPermissionBlockFactory(
        componentContext = childContext("appBlockerCardContentDecomposeComponent_permission")
    )
    private val appBlockerFilterCardContent = appBlockerFilterBlockFactory(
        componentContext = childContext("appBlockerCardContentDecomposeComponent_filter")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier.fillMaxWidth()
        ) {
            val isPermissionGranted by appBlockerPermissionCardContent.isAllPermissionGranted()
                .collectAsState()
            val isAppBlockingEnabled by appBlockerApi
                .isAppBlockerSupportActive()
                .collectAsState(false)

            AppBlockerHeaderComposable(
                checked = isAppBlockingEnabled,
                onSwitch = {
                    if (isAppBlockingEnabled) {
                        appBlockerApi.disableSupport()
                    } else {
                        appBlockerApi.enableSupport()
                    }
                },
                enabled = isPermissionGranted
            )

            if (isPermissionGranted) {
                appBlockerFilterCardContent.Render(
                    Modifier
                        .padding(vertical = 32.dp)
                        .systemBarsPadding()
                )
            } else {
                appBlockerPermissionCardContent.Render(Modifier.padding(top = 16.dp))
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerCardContentDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) -> AppBlockerCardContentDecomposeComponentImpl
    ) : AppBlockerCardContentDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) = factory(componentContext, onBackParameter)
    }
}
