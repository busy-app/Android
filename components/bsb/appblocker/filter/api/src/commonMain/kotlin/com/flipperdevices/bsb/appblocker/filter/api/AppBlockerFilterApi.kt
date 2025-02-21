package com.flipperdevices.bsb.appblocker.filter.api

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import kotlinx.coroutines.flow.Flow

interface AppBlockerFilterApi {
    suspend fun isBlocked(packageName: String): Boolean

    fun getBlockedAppCount(): Flow<BlockedAppCount>
}
