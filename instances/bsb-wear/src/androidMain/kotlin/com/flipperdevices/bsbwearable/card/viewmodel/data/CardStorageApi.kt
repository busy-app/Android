package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.OldTimerSettings
import kotlinx.coroutines.flow.StateFlow

interface CardStorageApi {
    val settingFlow: StateFlow<OldTimerSettings>
    val appBlockerFlow: StateFlow<BlockedAppCount?>
}
