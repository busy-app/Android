package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface CardStorageApi {
    val settingFlow: StateFlow<ImmutableList<WearOSTimerSettings>>
}
