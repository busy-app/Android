package com.flipperdevices.ui.cardframe

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.core.ui.res_preview.generated.resources.Res
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_metronome
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun FrameSectionInnerContent(
    painter: Painter,
    tint: Color,
    text: String,
    iconSize: Dp,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
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
            modifier = Modifier.size(iconSize)
        )
        Text(
            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
            text = text,
            maxLines = 1,
            color = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .primary,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                lineHeight = fontSize,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Bottom,
                    trim = LineHeightStyle.Trim.LastLineBottom
                ),
            )
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
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    fontSize: TextUnit = 18.sp,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 8.dp,
        horizontal = 12.dp
    ),
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFF).copy(0.2f))
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            miniFrameData.forEachIndexed { i, data ->
                val isLast = i == miniFrameData.lastIndex
                FrameSectionInnerContent(
                    painter = data.painter,
                    text = data.text,
                    tint = data.tint,
                    iconSize = iconSize,
                    fontSize = fontSize
                )
                if (!isLast) {
                    Box(
                        Modifier.padding(horizontal = 8.dp)
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
                    painter = painterResource(Res.drawable.ic_preview_metronome),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            )
            MiniFrameSection(
                MiniFrameData(
                    text = "25m",
                    painter = painterResource(Res.drawable.ic_preview_metronome),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                ),
                MiniFrameData(
                    text = "5m",
                    painter = painterResource(Res.drawable.ic_preview_metronome),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                ),
                MiniFrameData(
                    text = "20m",
                    painter = painterResource(Res.drawable.ic_preview_metronome),
                    tint = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            )
        }
    }
}
