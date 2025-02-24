package com.flipperdevices.bsb.appblocker.stats.api

import com.flipperdevices.bsb.appblocker.stats.model.AppLaunchRecordEvent

interface AppBlockerStatsApi {
    suspend fun recordBlockApp(event: AppLaunchRecordEvent)

    suspend fun getBlockAppCount(packageName: String): Int
}
