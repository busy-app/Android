package com.flipperdevices.bsb.timer.common.composable.appbar.stop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.core.ModalBottomSheetScope
import com.composables.core.Scrim

@Composable
fun ModalBottomSheetScope.InvisibleSheet(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Scrim()
        content()
    }
}
