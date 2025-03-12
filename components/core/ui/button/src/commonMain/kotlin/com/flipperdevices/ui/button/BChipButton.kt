package com.flipperdevices.ui.button

import BaselineText
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.core.ui.res_preview.generated.resources.Res
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_pomodoro
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("MagicNumber")
private val DASH_INTERVALS = floatArrayOf(10f, 10f)

fun Modifier.dashedBorder(
    color: Color,
    radius: Dp
): Modifier {
    return this.then(
        Modifier.drawBehind {
            drawRoundRect(
                color = color,
                style = Stroke(
                    width = 6f,
                    pathEffect = PathEffect.dashPathEffect(intervals = DASH_INTERVALS, phase = 0f)
                ),
                cornerRadius = CornerRadius(radius.toPx())
            )
        }
    )
}

@Composable
fun BChipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 46.dp,
        vertical = 24.dp
    ),
    enabled: Boolean = true,
    dashedBorderColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .then(
                if (dashedBorderColor == null) {
                    Modifier
                } else {
                    Modifier.dashedBorder(dashedBorderColor, 112.dp)
                }
            )
            .animateContentSize()
            .clip(RoundedCornerShape(112.dp))
            .background(background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun BChipButton(
    text: String?,
    painter: Painter?,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalCorruptedPallet.current.white.invert,
    background: Color = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
    dashedBorderColor: Color? = null,
    enabled: Boolean = true,
    fontSize: TextUnit = 24.sp,
    iconSize: Dp = 18.dp,
    spacedBy: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 16.dp
    ),
    onClick: () -> Unit
) {
    val animatedContentColor by animateColorAsState(contentColor)
    val animatedBackgroundColor by animateColorAsState(background)
    BChipButton(
        background = animatedBackgroundColor,
        modifier = modifier,
        onClick = onClick,
        dashedBorderColor = dashedBorderColor,
        enabled = enabled,
        contentPadding = contentPadding,
        content = {
            Row(
                modifier = Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    spacedBy,
                    Alignment.CenterHorizontally
                ),
                content = {
                    if (painter != null) {
                        Icon(
                            modifier = Modifier.size(iconSize),
                            painter = painter,
                            contentDescription = null,
                            tint = animatedContentColor
                        )
                    }

                    if (text != null) {
                        BaselineText(
                            text = text,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            color = animatedContentColor,
                            fontSize = fontSize,
                            lineHeight = fontSize,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.pragmatica,
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
@Suppress("LongMethod")
private fun BChipButtonPreview() {
    BusyBarThemeInternal {
        Scaffold(backgroundColor = Color.Black) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BChipButton(
                        text = "Hello",
                        painter = painterResource(Res.drawable.ic_preview_pomodoro),
                        onClick = {}
                    )
                    BChipButton(
                        text = "Hello",
                        painter = null,
                        onClick = {}
                    )
                    BChipButton(
                        text = null,
                        painter = painterResource(Res.drawable.ic_preview_pomodoro),
                        onClick = {}
                    )
                    BChipButton(
                        text = "New tag",
                        painter = painterResource(Res.drawable.ic_preview_pomodoro),
                        onClick = {},
                        background = Color.Transparent,
                        contentColor = LocalCorruptedPallet.current.transparent.whiteInvert.primary.copy(
                            alpha = 0.5f
                        ),
                        dashedBorderColor = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary.copy(
                            alpha = 0.1f
                        ),
                    )
                    listOf(Alignment.BottomStart, Alignment.TopEnd).forEach { alignment ->
                        BChipNotificationBox(
                            alignment = alignment,
                            chip = {
                                BChipButton(
                                    text = "My Tag",
                                    painter = painterResource(Res.drawable.ic_preview_pomodoro),
                                    onClick = {}
                                )
                            },
                            notification = {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_preview_pomodoro),
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
