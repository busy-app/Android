package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PauseFullScreenOverlayComposable(
    onStartClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 64.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .height(62.dp)
                                .width(20.dp)
                                .background(LocalCorruptedPallet.current.neutral.quaternary)
                        )
                    }
                }
                ButtonTimerComposable(
                    state = ButtonTimerState.START,
                    onClick = onStartClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewPauseOverlayComposable() {
    BusyBarThemeInternal {
        Scaffold(backgroundColor = Color.Red) {
            PauseFullScreenOverlayComposable(onStartClick = {})
        }
    }
}
