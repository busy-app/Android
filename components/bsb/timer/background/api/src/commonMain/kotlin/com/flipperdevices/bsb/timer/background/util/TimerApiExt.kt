package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.PauseData
import com.flipperdevices.bsb.timer.background.model.PauseType
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import kotlinx.datetime.Clock

fun TimerApi.updateState(block: (TimerTimestamp?) -> TimerTimestamp?) {
    val newState = block.invoke(getTimestampState().value)
    setTimestampState(newState)
}

fun TimerApi.togglePause(pauseType: PauseType = PauseType.NORMAL) {
    val state = getState().value as? ControlledTimerState.Running
    val pauseType = state?.pauseType
    if (pauseType != null) {
        pause(pauseType)
    } else {
        resume()
    }
}

fun TimerApi.pause(type: PauseType = PauseType.NORMAL) {
    updateState { state ->
        if (state?.pauseData == null) {
            state?.copy(pauseData = PauseData(type))
        } else {
            state
        }
    }
}

fun TimerApi.resume() {
    updateState { state ->
        val pause = state?.pauseData?.instant
        if (pause != null) {
            val diff = Clock.System.now() - pause
            state.copy(
                pauseData = null,
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
