package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlin.time.Duration.Companion.seconds

fun TimerApi.updateState(block: (ControlledTimerState) -> ControlledTimerState) {
    val newState = block.invoke(getState().value)
    setState(newState)
}

fun TimerApi.togglePause() {
    updateState { state ->
        when (state) {
            ControlledTimerState.Finished -> state
            ControlledTimerState.NotStarted -> state
            is ControlledTimerState.Running.LongRest -> state.copy(isOnPause = !state.isOnPause)
            is ControlledTimerState.Running.Rest -> state.copy(isOnPause = !state.isOnPause)
            is ControlledTimerState.Running.Work -> state.copy(isOnPause = !state.isOnPause)
        }
    }
}

fun TimerApi.stop() {
    setState(ControlledTimerState.NotStarted)
}

fun TimerApi.skip() {
    updateState { state ->
        when (state) {
            ControlledTimerState.Finished -> state
            ControlledTimerState.NotStarted -> state
            is ControlledTimerState.Running.LongRest -> state.copy(timeLeft = 0.seconds)
            is ControlledTimerState.Running.Rest -> state.copy(timeLeft = 0.seconds)
            is ControlledTimerState.Running.Work -> state.copy(timeLeft = 0.seconds)
        }
    }
}

fun TimerApi.startWith(settings: TimerSettings) {
    setState(
        ControlledTimerState.Running.Work(
            timeLeft = settings.intervalsSettings.work,
            isOnPause = false,
            timerSettings = settings,
            currentIteration = 0,
            maxIterations = when {
                settings.intervalsSettings.isEnabled -> {
                    settings.totalTime
                        .inWholeSeconds
                        .div(
                            settings.intervalsSettings
                                .work
                                .plus(settings.intervalsSettings.rest)
                                .plus(settings.intervalsSettings.longRest)
                                .inWholeSeconds
                        ).toInt()
                }

                else -> 0
            }
        )
    )
}
