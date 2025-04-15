package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.dao.model.CardSettingsId

/**
 * Object that can be used to calculate the current state of the timer
 *
 * Most often created in response to user actions
 *
 * Synchronised between clients
 */
data class RunningTimerState(
    val cardId: CardSettingsId,
    val stateTimestampMs: Long,
    val timerModeState: TimerModeState
) {
    sealed interface TimerModeState {
        data object Finished : TimerModeState

        data class Simple(
            val timeLeftMs: Long,
            val isPaused: Boolean,
        ) : TimerModeState

        data class Infinite(
            val timePassedMs: Long,
            val isPaused: Boolean,
        ) : TimerModeState

        data class Interval(
            val currentInterval: Int,
            val timeLeftMs: Long,
            val isPaused: Boolean,
        ) : TimerModeState
    }
}