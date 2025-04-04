package com.flipperdevices.bsb.timer.statefactory

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp

interface TimerStateFactory {
    fun create(timestamp: TimerTimestamp): ControlledTimerState
}
