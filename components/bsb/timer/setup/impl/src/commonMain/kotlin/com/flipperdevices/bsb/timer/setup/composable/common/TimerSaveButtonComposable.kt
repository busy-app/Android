package com.flipperdevices.bsb.timer.setup.composable.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.flipperdevices.ui.button.BChipButton

@Composable
fun TimerSaveButtonComposable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BChipButton(
            text = "Save",
            painter = null,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(fraction = 0.4f)
        )
    }
}
