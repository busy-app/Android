package com.flipperdevices.bsb.appblocker.permission.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.appblocker_permission_title
import busystatusbar.components.bsb.appblocker.permission.impl.generated.resources.ic_failed_status
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PermissionHeaderComposable(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(Res.drawable.ic_failed_status),
            contentDescription = null,
            tint = LocalCorruptedPallet.current.danger.primary
        )
        Text(
            modifier = Modifier.padding(vertical = 12.dp),
            text = stringResource(Res.string.appblocker_permission_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            color = LocalCorruptedPallet.current.danger.primary,
            fontFamily = LocalBusyBarFonts.current.pragmatica
        )
    }
}
