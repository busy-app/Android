package com.flipperdevices.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalPallet

@Composable
fun BSheetDragIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(5.dp)
            .fillMaxWidth(0.3f)
            .clip(RoundedCornerShape(100.dp))
            .background(LocalPallet.current.transparent.whiteInvert.tertiary.copy(0.1f))
    )
}
