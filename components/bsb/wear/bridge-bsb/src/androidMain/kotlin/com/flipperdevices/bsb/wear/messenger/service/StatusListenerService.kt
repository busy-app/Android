package com.flipperdevices.bsb.wear.messenger.service

import android.util.Log
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerRequestUpdateMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.core.di.ComponentHolder
import com.google.android.gms.wearable.MessageEvent

class StatusListenerService : WearableMessengerListenerService() {
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

    private fun MessageEvent.toMessage() = when (this.path) {
        PingMessage.serializer.path -> PingMessage.serializer
        PongMessage.serializer.path -> PongMessage.serializer
        TimerRequestUpdateMessage.serializer.path -> TimerRequestUpdateMessage.serializer
        TimerTimestampMessage.serializer.path -> TimerTimestampMessage.serializer

        TimerActionMessage.Finish.serializer.path -> TimerActionMessage.Finish.serializer
        TimerActionMessage.ConfirmNextStage.serializer.path -> TimerActionMessage.ConfirmNextStage.serializer
        TimerActionMessage.Pause.serializer.path -> TimerActionMessage.Pause.serializer
        TimerActionMessage.Restart.serializer.path -> TimerActionMessage.Restart.serializer
        TimerActionMessage.Resume.serializer.path -> TimerActionMessage.Resume.serializer
        TimerActionMessage.Skip.serializer.path -> TimerActionMessage.Skip.serializer
        TimerActionMessage.Stop.serializer.path -> TimerActionMessage.Stop.serializer
        else -> null
    }

    private fun receivePingMessage(messageEvent: MessageEvent) = kotlin.runCatching {
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
