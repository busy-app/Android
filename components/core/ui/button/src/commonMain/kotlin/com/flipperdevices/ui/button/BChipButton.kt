package com.flipperdevices.ui.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BChipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = LocalPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    contentColor: Color = LocalPallet.current.white.invert,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 46.dp,
        vertical = 24.dp
    ),
    enabled: Boolean = true,
    dashedBorderColor: Color? = null,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier
            .then(
                if (dashedBorderColor == null) {
                    Modifier
                } else {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = dashedBorderColor,
                            style = Stroke(
                                width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            ),
                            cornerRadius = CornerRadius(112.dp.toPx())
                        )
                    }
                }
            )
            .animateContentSize(),
        contentPadding = contentPadding,
        shape = RoundedCornerShape(112.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
            contentColor = contentColor
        ),
        onClick = onClick,
        enabled = enabled,
        content = content,
    )
}

@Composable
fun BChipButton(
    text: String?,
    painter: Painter?,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalPallet.current.white.invert,
    background: Color = LocalPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    dashedBorderColor: Color? = null,
    enabled: Boolean = true,
    fontSize: TextUnit = 24.sp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 46.dp,
        vertical = 24.dp
    ),
    onClick: () -> Unit
) {
    BChipButton(
        contentColor = contentColor,
        background = background,
        modifier = modifier,
        onClick = onClick,
        dashedBorderColor = dashedBorderColor,
        enabled = enabled,
        contentPadding = contentPadding,
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
                            style = PragmaticaTextStyle().copy(
                                textAlign = TextAlign.Start,
                                color = contentColor,
                                fontSize = fontSize,
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
fun BChipNotificationBox(
    chip: @Composable () -> Unit,
    notification: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center,
            content = { chip.invoke() }
        )
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = alignment,
            content = { notification.invoke() }
        )
    }
}

@Composable
@Preview
private fun BChipButtonPreview() {
    BusyBarThemeInternal {
        Scaffold(backgroundColor = Color.Black) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BChipButton(
                        text = "Hello",
                        painter = rememberVectorPainter(Icons.Filled.Call),
                        onClick = {}
                    )
                    BChipButton(
                        text = "Hello",
                        painter = null,
                        onClick = {}
                    )
                    BChipButton(
                        text = null,
                        painter = rememberVectorPainter(Icons.Filled.Call),
                        onClick = {}
                    )
                    BChipButton(
                        text = "New tag",
                        painter = rememberVectorPainter(Icons.Filled.Add),
                        onClick = {},
                        background = Color.Transparent,
                        contentColor = LocalPallet.current.transparent.whiteInvert.primary.copy(alpha = 0.5f),
                        dashedBorderColor = LocalPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
                    )
                    listOf(Alignment.BottomStart, Alignment.TopEnd).forEach { alignment ->
                        BChipNotificationBox(
                            alignment = alignment,
                            chip = {
                                BChipButton(
                                    text = "My Tag",
                                    painter = rememberVectorPainter(Icons.Filled.Call),
                                    onClick = {}
                                )
                            },
                            notification = {
                                Icon(
                                    painter = rememberVectorPainter(Icons.Filled.Delete),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .padding(4.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
