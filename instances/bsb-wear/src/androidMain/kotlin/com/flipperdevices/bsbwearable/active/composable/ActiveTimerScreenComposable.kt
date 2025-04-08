package com.flipperdevices.bsbwearable.active.composable

import BaselineText
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.tooling.preview.devices.WearDevices
import busystatusbar.components.bsb.timer.common.generated.resources.ic_next
import busystatusbar.components.bsb.timer.common.generated.resources.ic_pause
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwt_long_rest
import busystatusbar.instances.bsb_wear.generated.resources.bwt_pause
import busystatusbar.instances.bsb_wear.generated.resources.bwt_rest
import busystatusbar.instances.bsb_wear.generated.resources.tds_iteration_progress
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.currentUiIteration
import com.flipperdevices.bsb.timer.background.model.maxUiIterations
import com.flipperdevices.bsb.timer.background.model.progress
import com.flipperdevices.ui.autosizetext.AutoSizeText
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.button.BIconButton
import com.flipperdevices.ui.timeline.util.toFormattedTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CTRes

@Composable
private fun ControlledTimerState.InProgress.Running.rememberTimeLeftText(): String {
    return remember(timeLeft, timePassed) {
        val time = when (val localTimeLeft = timeLeft) {
            is TimerDuration.Finite -> {
                localTimeLeft.instance
            }

            TimerDuration.Infinite -> timePassed
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
}

@Composable
private fun ActiveTimerScreenTitle(
    timerState: ControlledTimerState.InProgress.Running,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val text = when (timerState) {
            is ControlledTimerState.InProgress.Running.LongRest -> stringResource(Res.string.bwt_long_rest)
            is ControlledTimerState.InProgress.Running.Rest -> stringResource(Res.string.bwt_rest)
            is ControlledTimerState.InProgress.Running.Work -> timerState.timerSettings.name
        }

        BaselineText(
            modifier = Modifier
                .background(timerState.getStatusColor(), RoundedCornerShape(160.dp))
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 1.dp),
            text = text,
            fontSize = 20.sp,
            color = LocalCorruptedPallet.current.white.onColor,
            fontFamily = LocalBusyBarFonts.current.pragmatica
        )
        if (timerState is ControlledTimerState.InProgress.Running.Work) {
            Text(
                text = when (timerState.timeLeft) {
                    is TimerDuration.Finite -> {
                        stringResource(
                            Res.string.tds_iteration_progress,
                            "${timerState.currentUiIteration}",
                            "${timerState.maxUiIterations}"
                        )
                    }

                    TimerDuration.Infinite -> "${timerState.currentUiIteration}"
                },
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                color = Color(color = 0x4DFFFFFF) // todo
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
internal fun ActiveTimerScreenComposable(
    timerState: ControlledTimerState,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (timerState) {
        is ControlledTimerState.InProgress.Await,
        is ControlledTimerState.Finished,
        ControlledTimerState.NotStarted -> Unit

        is ControlledTimerState.InProgress.Running -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ActiveTimerScreenTitle(timerState)
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BIconButton(
                            painter = painterResource(CTRes.drawable.ic_stop),
                            background = Color(color = 0x1AFFFFFF),
                            shape = CircleShape,
                            onClick = onStopClick,
                            modifier = Modifier.size(34.dp),
                        )
                        AutoSizeText(
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = 4.dp),
                            text = timerState.rememberTimeLeftText(),
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalCorruptedPallet.current.white.invert,
                            maxLines = 1
                        )
                        BIconButton(
                            painter = painterResource(CTRes.drawable.ic_next),
                            background = Color(color = 0x1AFFFFFF),
                            shape = CircleShape,
                            onClick = onSkipClick,
                            modifier = Modifier.size(34.dp),
                            enabled = when (timerState.timeLeft) {
                                is TimerDuration.Finite -> true
                                TimerDuration.Infinite -> false
                            }
                        )
                    }
                    if (timerState.timerSettings.totalTime is TimerDuration.Finite) {
                        LinearProgressIndicator(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            progress = animateFloatAsState(timerState.progress).value,
                            color = timerState.getStatusColor()
                        )
                    }
                }
                BChipButton(
                    onClick = onPauseClick,
                    text = stringResource(Res.string.bwt_pause),
                    painter = painterResource(CTRes.drawable.ic_pause),
                    contentColor = Color(color = 0x80FFFFFF),
                    background = Color(color = 0x1AFFFFFF),
                    modifier = Modifier,
                    fontSize = 16.sp,
                    iconSize = 12.dp,
                    spacedBy = 8.dp,
                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun ControlledTimerState.InProgress.Running.getStatusColor(): Color {
    return when (this) {
        is ControlledTimerState.InProgress.Running.LongRest,
        is ControlledTimerState.InProgress.Running.Rest -> Color(color = 0xFF00AC34) // todo
        is ControlledTimerState.InProgress.Running.Work ->
            LocalCorruptedPallet
                .current
                .accent
                .brand
                .primary
    }
}

@Preview(
    device = WearDevices.SMALL_ROUND
)
@Composable
private fun ActiveScreenComposablePreview() {
    var i by remember { mutableIntStateOf(0) }
    BusyBarThemeInternal {
        ActiveTimerScreenComposable(
            onPauseClick = {},
            onSkipClick = {
                i++
            },
            onStopClick = {},
            timerState = when (i % 3) {
                0 -> ControlledTimerState.InProgress.Running.Work(
                    timeLeft = TimerDuration.Finite(123.minutes),
                    isOnPause = false,
                    timerSettings = TimerSettings(
                        id = TimerSettingsId(id = -1),
                        intervalsSettings = TimerSettings.IntervalsSettings(
                            work = 150.minutes
                        )
                    ),
                    currentIteration = 0,
                    maxIterations = 3,
                    timePassed = 0.seconds
                )

                1 -> ControlledTimerState.InProgress.Running.Rest(
                    timeLeft = TimerDuration.Finite(12.minutes),
                    isOnPause = false,
                    timerSettings = TimerSettings(
                        id = TimerSettingsId(id = -1),
                        intervalsSettings = TimerSettings.IntervalsSettings(
                            work = 150.minutes
                        )
                    ),
                    currentIteration = 0,
                    maxIterations = 3,
                    timePassed = 0.seconds
                )

                else -> ControlledTimerState.InProgress.Running.LongRest(
                    timeLeft = TimerDuration.Finite(3.minutes),
                    isOnPause = false,
                    timerSettings = TimerSettings(
                        id = TimerSettingsId(id = -1),
                        intervalsSettings = TimerSettings.IntervalsSettings(
                            work = 150.minutes
                        )
                    ),
                    currentIteration = 0,
                    maxIterations = 3,
                    timePassed = 0.seconds
                )
            }
        )
    }
}
