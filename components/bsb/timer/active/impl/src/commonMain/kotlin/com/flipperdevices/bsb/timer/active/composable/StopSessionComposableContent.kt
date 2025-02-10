package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StopSessionComposableContent(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        Text(
            text = "Stop your focus session?",
            fontSize = 32.sp,
            color = LocalPallet.current
                .white
                .invert,
            fontWeight = FontWeight.W500,
            lineHeight = 32.sp,
        )
        Text(
            text = "This could interrupt your workflow and make it harder to get back on track.",
            fontSize = 16.sp,
            color = LocalPallet.current
                .neutral
                .tertiary,
            fontWeight = FontWeight.W500
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BChipButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
                painter = null,
                text = "Keep focusing",
                contentColor = LocalPallet.current
                    .white
                    .invert,
                background = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .tertiary
                    .copy(alpha = 0.1f),
            )
            BChipButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm,
                painter = null,
                text = "Stop",
                contentColor = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f),
                background = Color.Transparent,
            )
        }
    }
}

@Preview
@Composable
private fun StopSessionComposableContentPreview() {
    BusyBarThemeInternal {
        StopSessionComposableContent(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
