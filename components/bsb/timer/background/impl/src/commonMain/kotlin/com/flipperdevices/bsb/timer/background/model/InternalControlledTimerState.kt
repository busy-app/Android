package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.core.data.timer.TimerState
import kotlinx.datetime.Instant

internal data class InternalControlledTimerState(
    val timerState: TimerState,
    val pauseOn: Instant? = null
)

internal fun InternalControlledTimerState.toPublicState(): ControlledTimerState {
    return ControlledTimerState(
        timerState = timerState,
        isOnPause = pauseOn != null
    )
}
