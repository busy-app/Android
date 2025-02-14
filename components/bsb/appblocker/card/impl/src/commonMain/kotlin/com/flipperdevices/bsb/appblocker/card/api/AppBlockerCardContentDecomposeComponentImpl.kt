package com.flipperdevices.bsb.appblocker.card.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.appblocker.card.composable.AppBlockerHeaderComposable
import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
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
    appBlockerPermissionBlockFactory: AppBlockerPermissionBlockDecomposeComponent.Factory,
) : AppBlockerCardContentDecomposeComponent(componentContext) {
    private val appBlockerCardContent = appBlockerPermissionBlockFactory(
        componentContext = childContext("appBlockerCardContentDecomposeComponent_permission")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier.fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            AppBlockerHeaderComposable(
                enabled = false,
                onSwitch = {}
            )
            val isPermissionGranted by appBlockerCardContent.isAllPermissionGranted()
                .collectAsState()

            if (isPermissionGranted) {

            } else {
                appBlockerCardContent.Render(Modifier.padding(top = 32.dp))
            }

            Spacer(Modifier.height(16.dp))
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