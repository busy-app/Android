package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.busycard_appblocker_all
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.busycard_appblocker_edit
import busystatusbar.components.bsb.timer.common.generated.resources.ic_block
import busystatusbar.components.bsb.timer.common.generated.resources.ic_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_work
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.ui.timeline.util.toFormattedTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonTimerRes

@Suppress("LongMethod")
@Composable
fun BusyCardComposable(
    background: Color,
    name: String,
    blockerState: BlockedAppCount,
    settings: TimerSettings,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .padding(24.dp)
            .height(232.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert
                )
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onClick),
                    text = stringResource(Res.string.busycard_appblocker_edit),
                    fontSize = 18.sp,
                    color = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().animateContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
                ) {
                    Crossfade(settings.totalTime.toFormattedTime()) { totalTimeFormatted ->
                        Text(
                            text = totalTimeFormatted,
                            fontSize = 40.sp,
                            color = LocalCorruptedPallet.current
                                .white
                                .onColor
                        )
                    }
                    if (settings.intervalsSettings.isEnabled) {
                        MiniFrameSection(
                            MiniFrameData(
                                text = settings.intervalsSettings.work.toFormattedTime(slim = false),
                                painter = painterResource(CommonTimerRes.drawable.ic_work),
                                tint = LocalCorruptedPallet.current
                                    .transparent
                                    .whiteInvert
                                    .primary
                            ),
                            MiniFrameData(
                                text = settings.intervalsSettings.rest.toFormattedTime(slim = false),
                                painter = painterResource(CommonTimerRes.drawable.ic_rest),
                                tint = LocalCorruptedPallet.current
                                    .transparent
                                    .whiteInvert
                                    .primary
                            ),
                        )
                    }
                }
                val text = when (blockerState) {
                    BlockedAppCount.All -> stringResource(Res.string.busycard_appblocker_all)
                    is BlockedAppCount.Count -> if (blockerState.count > 0) {
                        blockerState.count.toString()
                    } else {
                        null
                    }

                    BlockedAppCount.NoPermission,
                    BlockedAppCount.TurnOff -> null
                }

                if (text != null) {
                    MiniFrameSection(
                        MiniFrameData(
                            text = text,
                            painter = painterResource(CommonTimerRes.drawable.ic_block),
                            tint = LocalCorruptedPallet.current
                                .transparent
                                .whiteInvert
                                .primary
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
                onClick = {},
                blockerState = BlockedAppCount.TurnOff
            )

            BusyCardComposable(
                background = Color.Blue,
                name = "Not so Busy!",
                onClick = {},
                blockerState = BlockedAppCount.All,
                settings = TimerSettings(
                    intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = true)
                )
            )
        }
    }
}
