package com.flipperdevices.bsb.wear.messenger.service

import android.util.Log
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.google.android.gms.wearable.MessageEvent
import kotlinx.coroutines.launch

class StatusListenerService : WearableMessengerListenerService() {
    override val TAG: String = "StatusListenerService"

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d(TAG, "onMessageReceived: ${messageEvent.path}")
        receivePingMessage(messageEvent)
    }

    private fun MessageEvent.toMessage() = when (this.path) {
        PingMessage.path -> PingMessage
        PongMessage.path -> PongMessage
        TimerTimestampMessage.path -> TimerTimestampMessage
        else -> null
    }

    private fun receivePingMessage(messageEvent: MessageEvent) = kotlin.runCatching {
        val wearMessageReceiver = wearMessengerModule.wearMessageConsumer
        val message = messageEvent.toMessage() ?: run {
            Log.d(TAG, "receivePingMessage: can't handle message ${messageEvent.path}")
            return@runCatching
        }
        wearMessageReceiver.consume(
            message = message,
            byteArray = messageEvent.data
        )
    }.onFailure(Throwable::printStackTrace)
}