package com.flipperdevices.ui.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalPallet

@Composable
fun OptionSeparator(
    modifier: Modifier = Modifier,
    color: Color = LocalPallet.current
        .transparent
        .whiteInvert
        .quaternary
        .copy(alpha = 0.05f)
) {
    Box(
        modifier
            .height(1.dp)
            .background(color)
    )
}
