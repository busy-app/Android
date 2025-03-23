package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import kotlinx.coroutines.flow.Flow

interface CardAppBlockerApi {
    fun getBlockedAppCount(cardId: Long): Flow<BlockedAppCount>

    suspend fun getBlockedAppDetailedState(cardId: Long): BlockedAppDetailedState

    suspend fun updateBlockedApp(cardId: Long, blockedAppState: BlockedAppDetailedState)
}