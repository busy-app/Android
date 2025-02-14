package com.flipperdevices.bsb.appblocker.permission.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.flipperdevices.bsb.appblocker.permission.composable.PermissionCardButtonComposable
import com.flipperdevices.bsb.appblocker.permission.composable.PermissionHeaderComposable
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerPermissionBlockDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : AppBlockerPermissionBlockDecomposeComponent(componentContext) {
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

            PermissionCardButtonComposable(
                modifier = Modifier,
                title = Res.string.appblocker_permission_usage_btn,
                onClick = {}
            )

            PermissionCardButtonComposable(
                modifier = Modifier.padding(top = 8.dp),
                title = Res.string.appblocker_permission_overlay_btn,
                onClick = {}
            )
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerPermissionBlockDecomposeComponent.Factory::class)
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