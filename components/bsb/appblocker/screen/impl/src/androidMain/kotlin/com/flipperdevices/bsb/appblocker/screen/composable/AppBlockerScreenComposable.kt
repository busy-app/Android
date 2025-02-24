package com.flipperdevices.bsb.appblocker.screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.screen.impl.generated.resources.appblocker_screen_btn
import com.flipperdevices.bsb.appblocker.screen.model.InternalApplicationInfo
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerScreenComposable(
    applicationInfo: InternalApplicationInfo,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        AppBlockerContentComposable(
            modifier = Modifier.fillMaxSize(),
            applicationInfo = applicationInfo
        )

        Text(
            modifier = Modifier
                .padding(bottom = 18.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(LocalPallet.current.transparent.blackInvert.quaternary)
                .clickableRipple(onClick = onBack)
                .padding(
                    vertical = 12.dp,
                    horizontal = 32.dp
                ),
            text = stringResource(Res.string.appblocker_screen_btn),
            color = LocalPallet.current.black.invert,
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
private fun AppBlockerScreenComposablePreview() {
    BusyBarThemeInternal {
        AppBlockerScreenComposable(
            applicationInfo = InternalApplicationInfo(
                packageName = "com.example",
                openCount = 10,
                name = "Test app"
            ),
            onBack = {}
        )
    }
}
