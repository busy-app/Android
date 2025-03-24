package com.flipperdevices.bsb.timer.cards.model

import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.TimerSettings

data class BusyCardModel(
    val settings: TimerSettings,
    val blockedAppCount: BlockedAppCount
)