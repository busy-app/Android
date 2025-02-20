package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState

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
