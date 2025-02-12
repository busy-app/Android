package com.flipperdevices.bsb.profile.main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalPallet

@Composable
fun SeparatorComposable(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxWidth()
            .height(1.dp)
            .background(LocalPallet.current.neutral.primary)
    )
}
