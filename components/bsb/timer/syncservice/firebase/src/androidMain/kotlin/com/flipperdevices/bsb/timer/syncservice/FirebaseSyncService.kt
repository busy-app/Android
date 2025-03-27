package com.flipperdevices.bsb.timer.syncservice

import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.syncservice.di.SyncServiceDIComponent
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class FirebaseSyncService : FirebaseMessagingService(), LogTagProvider {
    override val TAG: String = "MyFirebaseMessagingService"
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val serviceDIComponent: SyncServiceDIComponent
        get() = ComponentHolder.component<SyncServiceDIComponent>()

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data["data"]
        if (data.isNullOrEmpty()) {
            error { "#onMessageReceived: got message without data field" }
            return
        }
        val timerTimestamp = runCatching {
            json.decodeFromString<TimerTimestamp>(serializer(), data)
        }.onFailure {
            error(it) { "#onMessageReceived could not decode" }
        }.getOrNull() ?: return
        serviceDIComponent.timerApi.setTimestampState(timerTimestamp)
    }

    override fun onNewToken(token: String) {
        info { "#onNewToken: $token" }
        coroutineScope.launch {
            serviceDIComponent.krateApi.firebaseTokenKrate.save(token)
            serviceDIComponent.bsbMockApi.auth()
        }
    }
}
