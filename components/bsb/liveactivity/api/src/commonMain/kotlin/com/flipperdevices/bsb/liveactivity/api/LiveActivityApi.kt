package com.flipperdevices.bsb.liveactivity.api

import com.flipperdevices.core.data.timer.TimerState

interface LiveActivityApi {
    fun start(isOn: Boolean, state: TimerState) = Unit
    fun update(isOn: Boolean, state: TimerState) = Unit
    fun end() = Unit
}
