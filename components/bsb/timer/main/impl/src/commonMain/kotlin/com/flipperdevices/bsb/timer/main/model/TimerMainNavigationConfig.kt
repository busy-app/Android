package com.flipperdevices.bsb.timer.main.model

import com.flipperdevices.bsb.timer.background.model.TimerState
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerMainNavigationConfig {
    @Serializable
    data object Main : TimerMainNavigationConfig
    @Serializable
    data object Timer : TimerMainNavigationConfig
}