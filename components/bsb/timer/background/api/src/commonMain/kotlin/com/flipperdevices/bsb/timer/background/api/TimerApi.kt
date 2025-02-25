package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

interface TimerApi {
    fun setTimestampState(state: TimerTimestamp?)
    fun getTimestampState(): StateFlow<TimerTimestamp?>

    fun getState(): StateFlow<ControlledTimerState>
}

@Serializable
data class TimerTimestamp(
    val settings: TimerSettings,
    val start: Instant = Clock.System.now(),
    val pause: Instant? = null
)

val TimerTimestamp.isOnPause: Boolean
    get() = pause != null
