package com.flipperdevices.bsb.wear.messenger.service

import android.util.Log
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.core.di.ComponentHolder
import com.google.android.gms.wearable.MessageEvent

class WearListenerService : WearableMessengerListenerService() {
    override val TAG: String = "StatusListenerService"
    private val wearMessengerComponent by lazy {
        ComponentHolder.component<WearDataLayerModule>()
    }

    private val wearMessageConsumer: WearMessageConsumer by lazy {
        wearMessengerComponent.wearMessageConsumer
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d(TAG, "onMessageReceived: ${messageEvent.path}")
        receivePingMessage(messageEvent)
    }

    @Suppress("CyclomaticComplexMethod")
    private fun MessageEvent.toMessage() = when (this.path) {
        PingMessage.serializer.path -> PingMessage.serializer
        PongMessage.serializer.path -> PongMessage.serializer

        TimerTimestampRequestMessage.serializer.path -> TimerTimestampRequestMessage.serializer
        TimerTimestampMessage.Companion.serializer.path -> TimerTimestampMessage.Companion.serializer

        TimerActionMessage.Finish.serializer.path -> TimerActionMessage.Finish.serializer
        TimerActionMessage.ConfirmNextStage.serializer.path -> TimerActionMessage.ConfirmNextStage.serializer
        TimerActionMessage.Pause.serializer.path -> TimerActionMessage.Pause.serializer
        TimerActionMessage.Restart.serializer.path -> TimerActionMessage.Restart.serializer
        TimerActionMessage.Resume.serializer.path -> TimerActionMessage.Resume.serializer
        TimerActionMessage.Skip.serializer.path -> TimerActionMessage.Skip.serializer
        TimerActionMessage.Stop.serializer.path -> TimerActionMessage.Stop.serializer

        AppBlockerCountMessage.serializer.path -> AppBlockerCountMessage.serializer
        AppBlockerCountRequestMessage.serializer.path -> AppBlockerCountRequestMessage.serializer

        TimerSettingsMessage.serializer.path -> TimerSettingsMessage.serializer
        TimerSettingsRequestMessage.serializer.path -> TimerSettingsRequestMessage.serializer
        else -> null
    }

    private fun receivePingMessage(messageEvent: MessageEvent) = runCatching {
        val message = messageEvent.toMessage() ?: run {
            Log.d(TAG, "receivePingMessage: can't handle message ${messageEvent.path}")
            return@runCatching
        }
        wearMessageConsumer.consume(
            message = message,
            byteArray = messageEvent.data
        )
    }.onFailure(Throwable::printStackTrace)
}
