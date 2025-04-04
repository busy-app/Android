package com.flipperdevices.bsb.timer.common.composable.appbar.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.ui.autosizetext.AutoSizeText
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
                .fillMaxWidth()
                .background(config.videoBackgroundColor.copy(alpha = 0.8f))
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(13.dp))

            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TimerRow(
                    modifier = Modifier,
                    timerState = timerState
                )
            }

            if (timerState.timerSettings.totalTime is TimerDuration.Finite) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .background(config.progressBarBackgroundColor)
                        .height(6.dp)
                        .fillMaxWidth(),
                    progress = getTimerProgress(timerState),
                    color = config.progressBarColor,
                    backgroundColor = config.progressBarBackgroundColor
                )
            }
        }
    }
}

@Composable
private fun getTimerProgress(timerState: ControlledTimerState.InProgress.Running): Float {
    val timeLeft = when (val localTimeLeft = timerState.timeLeft) {
        is TimerDuration.Finite -> localTimeLeft.instance
        TimerDuration.Infinite -> return 1f
    }
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
    return remember(timeLeft, totalDuration) {
        when {
            timeLeft > totalDuration -> 1f
            totalDuration.inWholeSeconds == 0L -> 0f
            else -> timeLeft.inWholeSeconds / totalDuration.inWholeSeconds.toFloat()
        }
    }
}

@Composable
private fun TimerRow(
    timerState: ControlledTimerState.InProgress.Running,
    modifier: Modifier = Modifier
) {
    val text = remember(timerState.timeLeft, timerState.timePassed) {
        val time = when (val localTimeLeft = timerState.timeLeft) {
            is TimerDuration.Finite -> {
                localTimeLeft.instance
            }

            TimerDuration.Infinite -> timerState.timePassed
        }
        time.toComponents { days, hours, minutes, seconds, nanoseconds ->
            val timeComponentList = listOfNotNull(
                hours.takeIf { h -> h > 0 },
                minutes,
                seconds
            )

            timeComponentList.joinToString(
                separator = ":",
                prefix = "",
                transform = { timeComponent -> timeComponent.toFormattedTime() }
            )
        }
    }

    AutoSizeText(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.W500,
        fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
        color = LocalCorruptedPallet.current.white.invert,
        maxLines = 1
    )
}
