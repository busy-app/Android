package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import kotlinx.coroutines.flow.Flow

interface CardAppBlockerApi {
    fun getBlockedAppCount(cardId: TimerSettingsId): Flow<BlockedAppCount>

    fun getBlockedAppDetailedState(cardId: TimerSettingsId): Flow<BlockedAppDetailedState>

    suspend fun updateBlockedApp(cardId: TimerSettingsId, blockedAppState: BlockedAppDetailedState)

    suspend fun isBlocked(cardId: TimerSettingsId, appEntity: BlockedAppEntity): Boolean
}