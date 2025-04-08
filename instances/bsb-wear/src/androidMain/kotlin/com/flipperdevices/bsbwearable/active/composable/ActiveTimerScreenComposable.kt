package com.flipperdevices.bsbwearable.active.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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
            ActiveTimerScreenContentComposable(
                timerState = timerState,
                onPauseClick = onPauseClick,
                onStopClick = onStopClick,
                onSkipClick = onSkipClick,
                modifier = modifier
            )
        }
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
