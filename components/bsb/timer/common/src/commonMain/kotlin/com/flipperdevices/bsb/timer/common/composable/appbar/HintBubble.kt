package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

private class TriangleEdgeShape(val angle: Int, val arrowSize: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = size.width / 2 - angle, y = arrowSize.toFloat())
            lineTo(x = size.width / 2, y = 0f)
            lineTo(x = size.width / 2 + angle, y = arrowSize.toFloat())
        }
        return Outline.Generic(path = trianglePath)
    }
}

@Composable
fun HintBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = LocalPallet.current
                    .transparent
                    .blackInvert
                    .secondary
                    .copy(alpha = 0.3f),
                shape = TriangleEdgeShape(
                    angle = 32,
                    arrowSize = with(LocalDensity.current) {
                        24.dp.toPx().toInt()
                    }
                )
            )
            .padding(top = 24.dp)
            .clip(RoundedCornerShape(112.dp))
            .background(
                color = LocalPallet.current
                    .transparent
                    .blackInvert
                    .secondary
                    .copy(alpha = 0.3f)
            )
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = LocalPallet.current.transparent
                .whiteInvert
                .secondary
                .copy(alpha = 0.3f)
        )
    }
}

@Preview
@Composable
private fun HintBubblePreview() {
    BusyBarThemeInternal {
        HintBubble(
            modifier = Modifier.fillMaxWidth(),
            text = "What would you like to focus on?"
        )
    }
}
