package com.flipperdevices.bsb.appblockerscreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblockerscreen.impl.generated.resources.Res
import busystatusbar.components.bsb.appblockerscreen.impl.generated.resources.appblocker_screen_desc
import busystatusbar.components.bsb.appblockerscreen.impl.generated.resources.appblocker_screen_title
import busystatusbar.components.bsb.appblockerscreen.impl.generated.resources.pic_blocked
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerContentComposable(
    modifier: Modifier,
    applicationInfo: ApplicationInfo
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.pic_blocked),
            contentDescription = null
        )
        Text(
            text = stringResource(Res.string.appblocker_screen_title),
            color = LocalPallet.current.black.invert,
            fontSize = 34.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.appblocker_screen_desc, applicationInfo.name),
            color = LocalPallet.current.black.invert,
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )
    }
}