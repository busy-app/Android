package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlinx.coroutines.flow.StateFlow

interface TimerApi {
    fun setState(state: ControlledTimerState)

    fun getState(): StateFlow<ControlledTimerState>
}
