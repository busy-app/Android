package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.wear.messenger.serializer.DecodedWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalHorologistApi::class)
@Suppress("UnusedPrivateMember")
@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageConsumer::class)
class WearDataLayerRegistryMessageConsumer : WearMessageConsumer, LogTagProvider {
    override val TAG: String = "WearDataLayerRegistryMessageConsumer"
    private val messageChannel = MutableSharedFlow<DecodedWearMessage<*>>()
    override val messagesFlow: Flow<DecodedWearMessage<*>> = messageChannel.asSharedFlow()

    override fun <T> consume(message: WearMessageSerializer<T>, byteArray: ByteArray) {
        kotlin.runCatching {
            val decodedWearMessage = DecodedWearMessage(
                path = message.path,
                value = message.decode(byteArray)
            )
            runBlocking { messageChannel.emit(decodedWearMessage) }
        }.onFailure { throwable ->
            error(throwable) { "#consume: could not publish message: ${throwable.stackTraceToString()}" }
        }.onSuccess {
            info { "#consume: published message: ${message.path} $message" }
        }
    }
}
