package com.flipperdevices.ui.cardframe

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.core.ui.card_frame.generated.resources.Res
import busystatusbar.components.core.ui.card_frame.generated.resources.ic_arrow_right
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_pomodoro
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.core.ui.res_preview.generated.resources.Res as PreviewRes

@Suppress("LongMethod")
@Composable
fun MediumCardFrameComposable(
    title: String,
    desc: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = Color.Unspecified
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp))
            .background(
                LocalPallet.current
                    .transparent
                    .whiteInvert
                    .quaternary
                    .copy(alpha = 0.02f)
            )
            .clickable(enabled = true, onClick = onClick)
            .padding(12.dp)
            .alpha(animateFloatAsState(targetValue = if (enabled) 1f else 0.3f).value)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = LocalPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f).basicMarquee(iterations = Int.MAX_VALUE)
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.2f),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = icon,
                    tint = iconTint,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = desc,
                    color = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                        .copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
@Preview
private fun MediumCardFrameComposablePreview() {
    BusyBarThemeInternal {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(2) {
                    MediumCardFrameComposable(
                        modifier = Modifier.weight(1f),
                        title = "Blocked Apps",
                        desc = "On",
                        icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                        onClick = {},
                        iconTint = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .primary
                            .copy(alpha = 0.5f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(2) {
                    MediumCardFrameComposable(
                        modifier = Modifier.weight(1f),
                        title = "Blocked Apps",
                        desc = "On",
                        icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                        onClick = {},
                        enabled = false,
                        iconTint = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .primary
                            .copy(alpha = 0.5f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(2) {
                    MediumCardFrameComposable(
                        modifier = Modifier.weight(1f),
                        title = "Blocked Apps Very Very Long Text",
                        desc = "On",
                        icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                        onClick = {},
                        iconTint = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .primary
                            .copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
