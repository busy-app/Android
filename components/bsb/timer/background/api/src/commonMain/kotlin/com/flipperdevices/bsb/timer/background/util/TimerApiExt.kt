package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.PauseData
import com.flipperdevices.bsb.timer.background.model.PauseType
import kotlin.time.Duration.Companion.seconds
import kotlinx.datetime.Clock

fun TimerApi.updateState(block: (TimerTimestamp?) -> TimerTimestamp?) {
    val newState = block.invoke(getTimestampState().value)
    setTimestampState(newState)
}

fun TimerApi.togglePause() {
    updateState { state ->
        val pause = state?.pauseData?.instant
        if (pause != null) {
            val diff = Clock.System.now() - pause
            val extraTime = when (state.pauseData.type) {
                PauseType.AFTER_WORK -> 1.seconds
                PauseType.AFTER_REST -> 1.seconds
                PauseType.NORMAL -> 0.seconds
            }
            state.copy(
                pauseData = null,
                start = state.start.plus(diff).plus(extraTime)
            )
        } else {
            state?.copy(pauseData = PauseData(PauseType.NORMAL))
        }
    }
}

fun TimerApi.pause() {
    updateState { state ->
        if (state?.pauseData == null) {
            state?.copy(
                pauseData = PauseData(PauseType.NORMAL),
            )
        } else state
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
        } else state
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
