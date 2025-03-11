package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class WearableMessengerListenerService : WearableListenerService(), LogTagProvider {
    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        info { "#onCreate" }
    }

    override fun onDestroy() {
        super.onDestroy()
        info { "#onDestroy DataLayerListenerService" }
        scope.cancel()
    }
}
