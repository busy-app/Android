package com.flipperdevices.bsbwearable.interrupt.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
private fun WearDialogScrim() {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = Color(color = 0x90000000) // todo no color label in figma
            )
    )
}

@Composable
fun BWearDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    // Scrim not visible on some wear APIs, so we disable it and make our own
    WearDialogScrim()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
        content = {
            (LocalView.current.parent as? DialogWindowProvider)?.window?.setDimAmount(0f)
            Box(
                modifier = Modifier,
                content = { content.invoke() }
            )
        }
    )
}
