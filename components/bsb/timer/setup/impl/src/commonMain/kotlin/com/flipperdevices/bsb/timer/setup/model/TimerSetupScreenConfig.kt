package com.flipperdevices.bsb.timer.setup.model

import com.flipperdevices.bsb.dao.model.TimerSettingsId
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerSetupScreenConfig {
    @Serializable
    data class Main(val timerSettingsId: TimerSettingsId) : TimerSetupScreenConfig
}
