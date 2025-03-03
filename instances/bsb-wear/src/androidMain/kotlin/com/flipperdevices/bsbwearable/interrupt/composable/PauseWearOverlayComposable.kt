package com.flipperdevices.bsbwearable.interrupt.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwin_start
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.compose.layout.fillMaxRectangle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@Composable
internal fun PauseWearOverlayComposable(
    onStartClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        content = {
            Column(
                modifier = Modifier.fillMaxRectangle(),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .height(60.dp)
                                .width(18.dp)
                                .background(LocalCorruptedPallet.current.neutral.quaternary)
                        )
                    }
                }

                BChipButton(
                    modifier = Modifier,
                    onClick = onStartClick,
                    text = stringResource(Res.string.bwin_start),
                    painter = painterResource(CommonRes.drawable.ic_play),
                    contentColor = LocalCorruptedPallet.current.black.onColor,
                    background = LocalCorruptedPallet.current.white.onColor,
                    fontSize = 16.sp,
                    contentPadding = PaddingValues(
                        vertical = 10.dp,
                        horizontal = 14.dp
                    )
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewPauseOverlayComposable() {
    BusyBarThemeInternal {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        )
        PauseWearOverlayComposable(
            onStartClick = {}
        )
    }
}
