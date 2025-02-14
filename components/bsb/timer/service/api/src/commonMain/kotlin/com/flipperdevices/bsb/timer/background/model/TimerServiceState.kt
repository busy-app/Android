package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.preference.model.TimerSettings

sealed interface TimerServiceState {
    object Pending : TimerServiceState

    enum class Status {
        WORK, REST, LONG_REST
    }

    data class Started(
        val currentIteration: Int,
        val maxIteration: Int,
        val timerState: ControlledTimerState,
        val timerSettings: TimerSettings,
        val status: Status
    ) : TimerServiceState

    data object Finished : TimerServiceState
}