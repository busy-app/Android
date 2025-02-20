package com.flipperdevices.bsb.appblocker.permission.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.appblocker_permission_usage_btn
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.ic_next
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PermissionCardButtonComposable(
    title: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LocalPallet.current.transparent.whiteInvert.quinary)
            .clickableRipple(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f),
            text = stringResource(title),
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontSize = 18.sp,
            color = LocalPallet.current.white.invert
        )
        Icon(
            painter = painterResource(Res.drawable.ic_next),
            tint = LocalPallet.current.white.invert,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun PermissionCardButtonComposablePreview() {
    BusyBarThemeInternal {
        PermissionCardButtonComposable(
            title = Res.string.appblocker_permission_usage_btn,
            onClick = {}
        )
    }
}
