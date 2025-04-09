package com.flipperdevices.bsb.timer.controller

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp

interface TimerControllerApi {
    fun updateState(block: (TimerTimestamp) -> TimerTimestamp)

    fun pause()

    fun confirmNextStep()

    fun resume()

    fun stop()

    fun skip()

    fun startWith(settings: TimerSettings)

    fun interface Factory {
        operator fun invoke(
            timerApi: TimerApi
        ): TimerControllerApi
    }
}
