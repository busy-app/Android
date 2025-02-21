package com.flipperdevices.bsb.appblocker.stats.api

import com.flipperdevices.bsb.appblocker.stats.model.AppLaunchRecordEvent

interface AppBlockerStatsApi {
    fun recordBlockAppAsync(event: AppLaunchRecordEvent)

    suspend fun getBlockAppCount(packageName: String): Int
}