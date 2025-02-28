package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import kotlinx.coroutines.flow.StateFlow

interface TimerApi {
    fun setTimestampState(state: TimerTimestamp?)
    fun getTimestampState(): StateFlow<TimerTimestamp?>

    fun getState(): StateFlow<ControlledTimerState>
}
