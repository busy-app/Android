package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info

/**
 * [WearMessageSyncService] should automatically send synchronization data between devices
 */
interface WearMessageSyncService : LogTagProvider {

    fun onCreate() {
        info { "#onCreate" }
    }

    fun onDestroy() {
        info { "#onDestroy" }
    }
}
