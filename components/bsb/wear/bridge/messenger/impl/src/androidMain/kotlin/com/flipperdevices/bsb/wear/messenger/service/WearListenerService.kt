package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class WearListenerService : WearableListenerService(), LogTagProvider {
    override val TAG: String = "StatusListenerService"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val wearMessengerComponent by lazy {
        ComponentHolder.component<WearDataLayerModule>()
    }

    private val wearMessageConsumer: WearMessageConsumer by lazy {
        wearMessengerComponent.wearMessageConsumer
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        info { "#onMessageReceived ${messageEvent.path}" }
        receivePingMessage(messageEvent)
    }

    @Suppress("CyclomaticComplexMethod")
    private fun MessageEvent.toMessage() = when (this.path) {
        TimerTimestampRequestMessage.serializer.path -> TimerTimestampRequestMessage.serializer
        TimerTimestampMessage.Companion.serializer.path -> TimerTimestampMessage.Companion.serializer

        AppBlockerCountMessage.serializer.path -> AppBlockerCountMessage.serializer
        AppBlockerCountRequestMessage.serializer.path -> AppBlockerCountRequestMessage.serializer

        TimerSettingsMessage.serializer.path -> TimerSettingsMessage.serializer
        TimerSettingsRequestMessage.serializer.path -> TimerSettingsRequestMessage.serializer
        else -> {
            error { "#toMessage could not handle wear message ${this.path}" }
            null
        }
    }

    private fun receivePingMessage(messageEvent: MessageEvent) = runCatching {
        val message = messageEvent.toMessage() ?: return@runCatching
        wearMessageConsumer.consume(
            message = message,
            byteArray = messageEvent.data
        )
    }.onFailure(Throwable::printStackTrace)

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
