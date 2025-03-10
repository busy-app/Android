package com.flipperdevices.bsbwearable.card.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import busystatusbar.components.bsb.timer.common.generated.resources.ic_block
import busystatusbar.components.bsb.timer.common.generated.resources.ic_rest
import busystatusbar.components.bsb.timer.common.generated.resources.ic_work
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwca_blocked_all
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.ui.cardframe.MiniFrameData
import com.flipperdevices.ui.cardframe.MiniFrameSection
import com.flipperdevices.ui.timeline.util.toFormattedTime
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.rememberColumnState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonTimerRes

@Composable
private fun calculateBlockerText(blockerState: BlockedAppCount): String? {
    return when (blockerState) {
        BlockedAppCount.All -> stringResource(Res.string.bwca_blocked_all)
        is BlockedAppCount.Count -> if (blockerState.count > 0) {
            blockerState.count.toString()
        } else {
            null
        }

        BlockedAppCount.NoPermission,
        BlockedAppCount.TurnOff -> null
    }
}

private const val DESIGN_CARD_ASPECT_RATIO = 170f / 107f

@Suppress("LongMethod")
@Composable
fun WearCardComposable(
    settings: TimerSettings,
    blockerState: BlockedAppCount?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(DESIGN_CARD_ASPECT_RATIO)
            .clip(RoundedCornerShape(20.dp))
            .background(LocalCorruptedPallet.current.accent.brand.primary)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = settings.name,
            fontSize = 20.sp,
            color = LocalCorruptedPallet.current.white.onColor,
            fontWeight = FontWeight.W500
        )
        Spacer(Modifier.height(16.dp))

        Column {
            Text(
                text = settings.totalTime.toFormattedTime(slim = true),
                fontSize = 18.sp,
                color = LocalCorruptedPallet.current.white.onColor,
                fontWeight = FontWeight.W500
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                            text = settings.intervalsSettings.rest.toFormattedTime(slim = true),
                            painter = painterResource(CommonTimerRes.drawable.ic_rest),
                            tint = LocalCorruptedPallet.current
                                .transparent
                                .whiteInvert
                                .primary
                        ),
                        iconSize = 16.dp,
                        fontSize = 11.sp,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                blockerState?.let {
                    calculateBlockerText(blockerState)?.let { blockedText ->
                        MiniFrameSection(
                            MiniFrameData(
                                text = blockedText,
                                painter = painterResource(CommonTimerRes.drawable.ic_block),
                                tint = LocalCorruptedPallet.current
                                    .transparent
                                    .whiteInvert
                                    .primary
                            ),
                            iconSize = 16.dp,
                            fontSize = 11.sp,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Preview
@Composable
private fun PreviewWearCardComposable() {
    BusyBarThemeInternal {
        ScalingLazyColumn(
            columnState = rememberColumnState()
        ) {
            items(count = 4) {
                WearCardComposable(
                    settings = TimerSettings(
                        intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = true)
                    ),
                    blockerState = BlockedAppCount.Count(count = 24)
                )
            }
            items(count = 4) {
                WearCardComposable(
                    settings = TimerSettings(),
                    blockerState = BlockedAppCount.All
                )
            }
        }
    }
}
