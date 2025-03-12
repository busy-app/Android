package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlinx.coroutines.flow.StateFlow

interface CardStorageApi {
    val settingFlow: StateFlow<TimerSettings>
    val appBlockerFlow: StateFlow<BlockedAppCount?>
}
