package com.flipperdevices.bsb.wear.messenger.consumer

import android.util.Log
import com.flipperdevices.bsb.wear.messenger.serializer.DecodedWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalHorologistApi::class)
@Suppress("UnusedPrivateMember")
@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageConsumer::class)
class WearDataLayerRegistryMessageConsumer : WearMessageConsumer {
    private val messageChannel = Channel<DecodedWearMessage<*>>()
    override val messagesFlow: Flow<DecodedWearMessage<*>> = messageChannel.receiveAsFlow()

    override fun <T> consume(message: WearMessageSerializer<T>, byteArray: ByteArray) {
        kotlin.runCatching {
            val decodedWearMessage = DecodedWearMessage(
                path = message.path,
                value = message.decode(byteArray)
            )
            runBlocking { messageChannel.send(decodedWearMessage) }
        }.onFailure {
            Log.d(TAG, "consume: could not publish message: ${it.stackTraceToString()}")
        }.onSuccess {
            Log.d(TAG, "consume: published message: $message")
        }
    }

    companion object {
        private const val TAG = "WearMessageReceiver"
    }
}
