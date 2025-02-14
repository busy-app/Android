package com.flipperdevices.bsb.appblocker.card.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.appblocker_desc
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.appblocker_title
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerHeaderComposable(
    enabled: Boolean,
    onSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.appblocker_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                color = LocalPallet.current.white.invert,
                fontFamily = LocalBusyBarFonts.current.pragmatica
            )
            Switch(
                checked = enabled,
                onCheckedChange = onSwitch,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LocalPallet.current.accent.device.primary,
                    uncheckedThumbColor = LocalPallet.current.neutral.quaternary,
                    uncheckedTrackColor = LocalPallet.current.white.onColor
                )
            )
        }
        Text(
            text = stringResource(Res.string.appblocker_desc),
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = LocalPallet.current.neutral.tertiary
        )
    }
}