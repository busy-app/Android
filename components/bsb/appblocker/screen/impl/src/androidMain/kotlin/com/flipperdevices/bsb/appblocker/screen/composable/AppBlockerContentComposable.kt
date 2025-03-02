package com.flipperdevices.bsb.appblocker.screen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.appblocker_screen_desc
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.appblocker_screen_title
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.pic_blocked
import com.flipperdevices.bsb.appblocker.screen.model.InternalApplicationInfo
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerContentComposable(
    applicationInfo: InternalApplicationInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val drawable = remember(applicationInfo.packageName, context) {
            runCatching {
                context.packageManager.getApplicationIcon(context.packageName)
            }.getOrNull()
        }

        Image(
            modifier = Modifier
                .size(92.dp),
            painter = if (drawable == null) {
                painterResource(Res.drawable.pic_blocked)
            } else {
                rememberDrawablePainter(drawable)
            },
            contentDescription = applicationInfo.name
        )

        Text(
            text = stringResource(
                Res.string.appblocker_screen_title,
                applicationInfo.name
            ),
            color = LocalPallet.current.black.invert,
            fontSize = 34.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(
                Res.string.appblocker_screen_desc,
                applicationInfo.openCount
            ),
            color = LocalPallet.current.black.invert,
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )
    }
}
