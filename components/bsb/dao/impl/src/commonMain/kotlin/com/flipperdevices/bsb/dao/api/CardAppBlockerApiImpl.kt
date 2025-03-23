package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import kotlinx.coroutines.flow.Flow

class CardAppBlockerApiImpl : CardAppBlockerApi {
    override fun getBlockedAppCount(cardId: Long): Flow<BlockedAppCount> {
        TODO("Not yet implemented")
    }

    override suspend fun getBlockedAppDetailedState(cardId: Long): BlockedAppDetailedState {
        TODO("Not yet implemented")
    }

    override suspend fun updateBlockedApp(cardId: Long, blockedAppState: BlockedAppDetailedState) {
        TODO("Not yet implemented")
    }
}