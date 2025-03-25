package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.dao.model.TimerSettings

interface TimerStateListener {
    suspend fun onTimerStart(timerSettings: TimerSettings) = Unit
    suspend fun onTimerStop() = Unit
}
