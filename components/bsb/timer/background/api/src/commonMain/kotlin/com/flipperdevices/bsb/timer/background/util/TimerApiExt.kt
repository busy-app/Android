package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun TimerApi.updateState(block: (TimerTimestamp?) -> TimerTimestamp?) {
    val newState = block.invoke(getTimestampState().value)
    setTimestampState(newState)
}

fun TimerApi.pause() {
    updateState { state ->
        if (state?.pause == null) {
            state?.copy(pause = Clock.System.now())
        } else {
            state
        }
    }
}

fun TimerApi.confirmNextSte() {
    val awaitState = getState().value as? ControlledTimerState.Await ?: return
    updateState { state ->
        state ?: return@updateState state
        state.copy(
            confirmNextStepClick = Clock.System.now().plus(1.seconds),
            start = state.start.plus(Clock.System.now().minus(awaitState.pausedAt))
        )
    }
}

fun TimerApi.resume() {
    updateState { state ->
        if (state?.pause != null) {
            val diff = Clock.System.now() - state.pause
            state.copy(
                pause = null,
                start = state.start.plus(diff)
            )
        } else {
            state
        }
    }
}

fun TimerApi.stop() {
    setTimestampState(null)
}

fun TimerApi.skip() {
    updateState { state ->
        state ?: return@updateState state
        val startedState = getState().value as? ControlledTimerState.Running ?: return@updateState state

        state.copy(start = state.start.minus(startedState.timeLeft))
    }
}

fun TimerApi.startWith(settings: TimerSettings) {
    setTimestampState(TimerTimestamp(settings = settings))
}
