package com.flipperdevices.bsb.preferencescreen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.Res
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_selected_app_blocker_title
import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.stringResource

val LocalFamilyControlApi = staticCompositionLocalOf<FamilyControlApi> {
    error("No FamilyControlApi provided")
}

@Composable
actual fun SettingsSelectedAppBlockerComposable(
    blockedApps: Int,
    onUpdate: () -> Unit,
    modifier: Modifier
) {
    val localViewController = LocalUIViewController.current
    val localFamilyControlApi = LocalFamilyControlApi.current

    Column(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .background(LocalPallet.current.transparent.blackInvert.quaternary)
            .padding(12.dp)
            .clickableRipple(bounded = false) {
                val vc = localFamilyControlApi.getVC { onUpdate() }

                localViewController.presentViewController(
                    viewControllerToPresent = vc,
                    animated = true,
                    completion = null
                )
            },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.preference_selected_app_blocker_title),
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current.neutral.tertiary,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = blockedApps.toString(),
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current.neutral.tertiary,
                fontSize = 16.sp
            )
        }
    }
}
