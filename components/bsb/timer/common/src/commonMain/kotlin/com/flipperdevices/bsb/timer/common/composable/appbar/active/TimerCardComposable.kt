package com.flipperdevices.bsb.timer.common.composable.appbar.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceIn
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.ui.timeline.util.toFormattedTime
import com.flipperdevices.ui.video.BSBVideoPlayer

private const val HD_RATIO = 16f / 9f

@Composable
fun TimerCardComposable(
    timerState: ControlledTimerState.InProgress.Running,
    config: TimerActiveConfiguration,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(24.dp))
    ) {
        var heightPx by remember { mutableStateOf<Int?>(null) }
        BSBVideoPlayer(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(HD_RATIO)
                .onGloballyPositioned { coordinates ->
                    heightPx = coordinates.size.height
                },
            uri = config.videoUri,
            firstFrame = config.firstFrame
        )

        var columnModifier: Modifier = Modifier
        heightPx?.let { heightPxLocal ->
            columnModifier = columnModifier.height(
                LocalDensity.current.run {
                    heightPxLocal.toDp()
                }
            )
        }
        Column(
            modifier = columnModifier
                .background(config.videoBackgroundColor.copy(alpha = 0.8f))
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(13.dp))

            TimerRow(timerState)

            val totalDuration = remember(timerState.timerSettings.intervalsSettings) {
                when (timerState) {
                    is ControlledTimerState.InProgress.Running.LongRest ->
                        timerState.timerSettings.intervalsSettings.longRest

                    is ControlledTimerState.InProgress.Running.Rest ->
                        timerState.timerSettings.intervalsSettings.rest

                    is ControlledTimerState.InProgress.Running.Work ->
                        timerState.timerSettings.intervalsSettings.work
                }
            }
            val progress = remember(timerState.timeLeft, totalDuration) {
                1f - (timerState.timeLeft / totalDuration).toFloat().fastCoerceIn(0f, 1f)
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.5.dp)
                    .height(6.dp),
                progress = progress,
                color = config.progressBarColor,
                backgroundColor = config.progressBarBackgroundColor
            )
        }
    }
}

@Composable
private fun TimerRow(
    timerState: ControlledTimerState.InProgress.Running,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        timerState.timeLeft.toComponents { days, hours, minutes, seconds, nanoseconds ->
            val timeComponentList = listOfNotNull(
                hours.takeIf { h -> h > 0 },
                minutes,
                seconds
            )
            Text(
                text = timeComponentList.joinToString(
                    separator = ":",
                    prefix = "",
                    transform = { timeComponent -> timeComponent.toFormattedTime() }
                ),
                style = TextStyle(
                    fontSize = 64.sp,
                    fontWeight = FontWeight.W500,
                    fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                    color = LocalCorruptedPallet.current.white.invert
                )
            )
        }
    }
}
