package com.flipperdevices.ui.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BChipButton(
    contentColor: Color = LocalPallet.current.white.invert,
    background: Color = LocalPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.animateContentSize(),
        contentPadding = PaddingValues(
            horizontal = 46.dp,
            vertical = 24.dp
        ),
        shape = RoundedCornerShape(112.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
            contentColor = contentColor
        ),
        onClick = onClick,
        enabled = enabled,
        content = content
    )
}

@Composable
fun BChipButton(
    text: String?,
    painter: Painter? = null,
    contentColor: Color = LocalPallet.current.white.invert,
    background: Color = LocalPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    BChipButton(
        contentColor = contentColor,
        background = background,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        content = {
            Row(
                modifier = Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                content = {
                    if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = contentColor
                        )
                    }
                    if (text != null) {
                        Text(
                            modifier = Modifier,
                            text = text,
                            maxLines = 1,
                            style = TextStyle(
                                textAlign = TextAlign.Start,
                                lineHeight = 24.sp,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Bottom,
                                    trim = LineHeightStyle.Trim.LastLineBottom
                                ),
                                color = contentColor,
                                fontFamily = LocalBusyBarFonts.current.pragmatica,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W500,
                            )
                        )
                    }
                }
            )
        }
    )
}

@Composable
@Preview
private fun BChipButtonPreview() {
    BusyBarThemeInternal {
        BChipButton(
            text = "Hello",
            painter = rememberVectorPainter(Icons.Filled.Call),
            onClick = {}
        )
    }
}