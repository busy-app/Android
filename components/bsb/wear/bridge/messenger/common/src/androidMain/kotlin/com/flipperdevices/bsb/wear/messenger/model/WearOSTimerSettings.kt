package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlinx.serialization.Serializable

@Serializable
data class WearOSTimerSettings(
    val instance: TimerSettings,
    val blockedAppCount: BlockedAppCount
)