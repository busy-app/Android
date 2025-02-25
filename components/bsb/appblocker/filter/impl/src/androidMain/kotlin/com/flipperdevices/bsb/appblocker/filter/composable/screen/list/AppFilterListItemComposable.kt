package com.flipperdevices.bsb.appblocker.filter.composable.screen.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_other
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppFilterListItemComposable(
    appInfo: UIAppInformation,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickableRipple { onClick(!appInfo.isBlocked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.size(24.dp),
            selected = appInfo.isBlocked,
            onClick = { onClick(!appInfo.isBlocked) },
            colors = RadioButtonDefaults.colors(
                selectedColor = LocalCorruptedPallet.current.accent.device.primary,
                unselectedColor = LocalCorruptedPallet.current.transparent.whiteInvert.quaternary
            )
        )

        val context = LocalContext.current
        val drawable = remember(appInfo.packageName, context) {
            runCatching {
                context.packageManager.getApplicationIcon(appInfo.packageName)
            }.getOrNull()
        }

        Image(
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .size(32.dp),
            painter = if (drawable == null) {
                painterResource(Res.drawable.ic_app_type_other)
            } else {
                rememberDrawablePainter(drawable)
            },
            contentDescription = appInfo.appName
        )

        Text(
            text = appInfo.appName,
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = Color(color = 0xFFFFFFFF),
        )
    }
}
