package com.flipperdevices.bsb.timer.background.statefactory

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp

interface TimerStateFactory {
    fun create(timestamp: TimerTimestamp): ControlledTimerState
}
