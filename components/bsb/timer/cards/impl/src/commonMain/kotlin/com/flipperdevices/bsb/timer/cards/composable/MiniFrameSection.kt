package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_long_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_work
import com.composables.core.Icon
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun FrameSectionInnerContent(
    painter: Painter,
    tint: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painter,
            tint = tint,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            color = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .primary,
            fontSize = 18.sp
        )
    }
}

data class MiniFrameData(
    val painter: Painter,
    val text: String,
    val tint: Color
)

@Suppress("MagicNumber") // todo this color is not present in LocalCorruptedPallet
@Composable
fun MiniFrameSection(
    vararg miniFrameData: MiniFrameData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFF).copy(0.2f))
            .padding(vertical = 8.dp)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            miniFrameData.forEachIndexed { i, data ->
                val isLast = i == miniFrameData.lastIndex
                FrameSectionInnerContent(
                    painter = data.painter,
                    text = data.text,
                    tint = data.tint
                )
                if (!isLast) {
                    Box(
                        Modifier.padding(horizontal = 5.dp)
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFFFFF).copy(0.2f))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMiniFrameSection() {
    BusyBarThemeInternal {
        Column {
            MiniFrameSection(
                MiniFrameData(
                    text = "25m",
                    painter = painterResource(Res.drawable.ic_rest),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            )
            MiniFrameSection(
                MiniFrameData(
                    text = "25m",
                    painter = painterResource(Res.drawable.ic_rest),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                ),
                MiniFrameData(
                    text = "5m",
                    painter = painterResource(Res.drawable.ic_long_rest),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                ),
                MiniFrameData(
                    text = "20m",
                    painter = painterResource(Res.drawable.ic_work),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            )
        }
    }
}
