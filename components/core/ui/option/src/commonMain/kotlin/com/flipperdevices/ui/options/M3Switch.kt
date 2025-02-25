package com.flipperdevices.ui.options

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet

@Composable
fun M3Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            uncheckedBorderColor = Color.Transparent,
            uncheckedThumbColor = LocalCorruptedPallet.current.white.onColor,
            checkedThumbColor = LocalCorruptedPallet.current.white.onColor,
            checkedBorderColor = Color.Transparent,
            checkedTrackColor = LocalCorruptedPallet.current.accent.device.primary,
            uncheckedTrackColor = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary,
            // todo no color for disabled in design system yet
//            checkedIconColor = TODO(),
//            disabledCheckedBorderColor = TODO(),
//            disabledCheckedIconColor = TODO(),
//            disabledCheckedThumbColor = TODO(),
//            disabledCheckedTrackColor = TODO(),
//            disabledUncheckedBorderColor = TODO(),
//            disabledUncheckedIconColor = TODO(),
//            disabledUncheckedThumbColor = TODO(),
//            uncheckedIconColor = TODO(),
//            disabledUncheckedTrackColor = TODO(),
        )
    )
}
