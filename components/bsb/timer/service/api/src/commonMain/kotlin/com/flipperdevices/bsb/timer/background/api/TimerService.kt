package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.TimerServiceState
import kotlinx.coroutines.flow.StateFlow

interface TimerService {

    val state: StateFlow<TimerServiceState>

    /**
     * Start service with specific settings
     */
    fun startWith(timerSettings: TimerSettings): Unit

    /**
     * Pause timer
     */
    fun togglePause()

    /**
     * Skip current state(work/rest/long rest)
     */
    fun skip()

    /**
     * Stop timer entirely
     */
    fun stop()
}
