package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_three_dots
import busystatusbar.components.bsb.timer.common.generated.resources.ic_block
import busystatusbar.components.bsb.timer.common.generated.resources.ic_long_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_rest
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.ui.timeline.toFormattedTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonTimerRes

@Suppress("LongMethod")
@Composable
fun BusyCardComposable(
    background: Color,
    name: String,
    settings: TimerSettings,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    color = LocalPallet.current
                        .white
                        .invert
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_three_dots),
                    contentDescription = null,
                    tint = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = settings.totalTime.toFormattedTime(),
                    fontSize = 40.sp,
                    color = LocalPallet.current
                        .white
                        .onColor
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (settings.intervalsSettings.isEnabled) {
                        MiniFrameSection(
                            MiniFrameData(
                                text = settings.intervalsSettings.rest.toFormattedTime(),
                                painter = painterResource(CommonTimerRes.drawable.ic_rest),
                                tint = LocalPallet.current
                                    .transparent
                                    .whiteInvert
                                    .primary
                            ),
                            MiniFrameData(
                                text = settings.intervalsSettings.longRest.toFormattedTime(),
                                painter = painterResource(CommonTimerRes.drawable.ic_long_rest),
                                tint = LocalPallet.current
                                    .transparent
                                    .whiteInvert
                                    .primary
                            ),
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                    }

                    MiniFrameSection(
                        MiniFrameData(
                            text = "14", // todo
                            painter = painterResource(CommonTimerRes.drawable.ic_block),
                            tint = LocalPallet.current
                                .transparent
                                .whiteInvert
                                .primary
                        ),
                        modifier = Modifier.align(
                            when {
                                settings.intervalsSettings.isEnabled -> Alignment.CenterEnd
                                else -> Alignment.CenterStart
                            }
                        ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBusyCardComposable() {
    BusyBarThemeInternal {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BusyCardComposable(
                background = Color.Red,
                name = "BUSY",
                settings = TimerSettings(),
                onClick = {}
            )

            BusyCardComposable(
                background = Color.Blue,
                name = "Not so Busy!",
                onClick = {},
                settings = TimerSettings(
                    intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = true)
                )
            )
        }
    }
}
