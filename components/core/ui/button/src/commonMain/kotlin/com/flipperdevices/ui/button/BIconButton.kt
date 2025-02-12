package com.flipperdevices.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import busystatusbar.components.core.ui.res_preview.generated.resources.Res
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_pomodoro
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    background: Color = LocalPallet.current.transparent.whiteInvert.quaternary,
    tint: Color = LocalPallet.current.transparent.whiteInvert.primary,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
@Preview
private fun BIconButtonPreview() {
    BusyBarThemeInternal {
        BIconButton(
            modifier = Modifier.size(48.dp),
            painter = painterResource(Res.drawable.ic_preview_pomodoro),
            onClick = {},
        )
    }
}
