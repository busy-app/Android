package com.flipperdevices.bsb.appblocker.permission.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.appblocker_permission_desc
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.appblocker_permission_overlay_btn
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.appblocker_permission_usage_btn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.flipperdevices.bsb.appblocker.permission.composable.PermissionCardButtonComposable
import com.flipperdevices.bsb.appblocker.permission.composable.PermissionHeaderComposable
import com.flipperdevices.bsb.appblocker.permission.utils.PermissionStateHolder
import com.flipperdevices.bsb.appblocker.permission.viewmodel.AppBlockerPermissionViewModel
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerPermissionBlockDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val stateHolder: PermissionStateHolder,
    private val appBlockerViewModelFactory: () -> AppBlockerPermissionViewModel
) : AppBlockerPermissionBlockDecomposeComponent(componentContext) {
    private val scope = coroutineScope(FlipperDispatchers.default)

    private val viewModel = viewModelWithFactory(null) {
        appBlockerViewModelFactory()
    }

    init {
        lifecycle.doOnResume { stateHolder.invalidate() }
    }

    override fun isAllPermissionGranted(): StateFlow<Boolean> {
        return stateHolder.getState().map {
            it.isAllPermissionGranted
        }.stateIn(scope, SharingStarted.Eagerly, stateHolder.getStateSync().isAllPermissionGranted)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(LocalPallet.current.transparent.whiteInvert.quinary)
                .padding(16.dp),
        ) {
            PermissionHeaderComposable()

            Text(
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp),
                text = stringResource(Res.string.appblocker_permission_desc),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current.transparent.whiteInvert.primary,
                fontFamily = LocalBusyBarFonts.current.pragmatica
            )

            val permissionState by stateHolder.getState().collectAsState()

            if (permissionState.hasUsageStatsPermission.not()) {
                PermissionCardButtonComposable(
                    modifier = Modifier,
                    title = Res.string.appblocker_permission_usage_btn,
                    onClick = viewModel::requestUsageStatsPermission
                )
            }

            if (permissionState.hasDrawOverlayPermission.not()) {
                PermissionCardButtonComposable(
                    modifier = Modifier.padding(top = 8.dp),
                    title = Res.string.appblocker_permission_overlay_btn,
                    onClick = viewModel::requestDrawOverlayPermission
                )
            }
        }
    }

    @Inject
    @ContributesBinding(
        AppGraph::class,
        AppBlockerPermissionBlockDecomposeComponent.Factory::class
    )
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> AppBlockerPermissionBlockDecomposeComponentImpl
    ) : AppBlockerPermissionBlockDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
