package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

private fun TimerApi.updateState(
    block: (TimerTimestamp) -> TimerTimestamp
) {
    val newState = block.invoke(getTimestampState().value)
    setTimestampState(newState)
}

fun TimerApi.pause() {
    updateState { state ->
        when (state) {
            is TimerTimestamp.Pending -> state
            is TimerTimestamp.Running -> {
                if (state.pause == null) {
                    state.copy(
                        pause = Clock.System.now(),
                        lastSync = Clock.System.now()
                    )
                } else {
                    state
                }
            }
        }
    }
}

fun TimerApi.confirmNextStep() {
    val awaitState = getState().value as? ControlledTimerState.InProgress.Await ?: return
    updateState { state ->
        when (state) {
            is TimerTimestamp.Pending -> state
            is TimerTimestamp.Running -> {
                state.copy(
                    confirmNextStepClick = Clock.System.now().plus(1.seconds),
                    start = state.start.plus(Clock.System.now().minus(awaitState.pausedAt)),
                    lastSync = Clock.System.now()
                )
            }
        }
    }
}

fun TimerApi.resume() {
    updateState { state ->
        when (state) {
            is TimerTimestamp.Pending -> state
            is TimerTimestamp.Running -> {
                if (state.pause != null) {
                    val diff = Clock.System.now() - state.pause
                    state.copy(
                        pause = null,
                        start = state.start.plus(diff),
                        confirmNextStepClick = state.confirmNextStepClick.plus(diff),
                        lastSync = Clock.System.now()
                    )
                } else {
                    state
                }
            }
        }
    }
}

fun TimerApi.stop() {
    updateState {
        TimerTimestamp.Pending.Finished
    }
}

fun TimerApi.skip() {
    updateState { state ->
        when (state) {
            is TimerTimestamp.Pending -> state
            is TimerTimestamp.Running -> {
                val startedState = getState().value as? ControlledTimerState.InProgress.Running
                startedState ?: return@updateState state

                state.copy(
                    start = state.start.minus(startedState.timeLeft),
                    lastSync = Clock.System.now()
                )
            }
        }
    }
}

fun TimerApi.startWith(settings: TimerSettings) {
    setTimestampState(
        state = TimerTimestamp.Running(
            settings = settings,
            lastSync = Clock.System.now()
        ),
    )
}
