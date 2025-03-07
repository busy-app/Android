package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.wear.messenger.serializer.DecodedWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import kotlinx.coroutines.flow.Flow

interface WearMessageConsumer {
    val messagesFlow: Flow<DecodedWearMessage<*>>
    fun <T> consume(message: WearMessageSerializer<T>, byteArray: ByteArray)
}
