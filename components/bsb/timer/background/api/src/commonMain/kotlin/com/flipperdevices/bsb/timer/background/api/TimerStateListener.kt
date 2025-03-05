package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.preference.model.TimerSettings

interface TimerStateListener {
    fun onTimerStart(timerSettings: TimerSettings) = Unit
    fun onTimerStop() = Unit
}
