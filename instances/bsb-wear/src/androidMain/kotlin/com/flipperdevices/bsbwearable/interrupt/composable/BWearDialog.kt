package com.flipperdevices.bsbwearable.interrupt.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun WearDialogScrim() {
    val hazeState = remember { HazeState() }
    Box(
        Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color(color = 0x000000).copy(alpha = 0.6f) // todo no color in pallet
                )
                .hazeEffect(
                    state = remember { HazeState() },
                    style = HazeMaterials.ultraThin(
                        containerColor = Color.Black.copy(alpha = 0.8f)
                    )
                )
        )
    }
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
