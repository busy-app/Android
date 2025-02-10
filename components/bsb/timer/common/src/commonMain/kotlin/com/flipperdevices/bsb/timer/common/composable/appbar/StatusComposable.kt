package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_busy
import busystatusbar.components.bsb.timer.common.generated.resources.ic_busy_long_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_busy_off
import busystatusbar.components.bsb.timer.common.generated.resources.ic_busy_rest
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatusComposable(
    type: StatusType,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Icon(
        painter = type.toPainter(),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = Color.Unspecified
    )
}

enum class StatusType {
    BUSY, OFF, REST, LONG_REST
}

private fun StatusType.toResource() = when (this) {
    StatusType.BUSY -> Res.drawable.ic_busy
    StatusType.OFF -> Res.drawable.ic_busy_off
    StatusType.REST -> Res.drawable.ic_busy_rest
    StatusType.LONG_REST -> Res.drawable.ic_busy_long_rest
}

@Composable
private fun StatusType.toPainter(): Painter {
    return painterResource(toResource())
}

@Composable
@Preview
private fun StatusComposablePreview() {
    BusyBarThemeInternal {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusType.entries.forEach { type ->
                StatusComposable(
                    type = type,
                    modifier = Modifier.height(45.dp)
                )
            }
        }
    }
}
