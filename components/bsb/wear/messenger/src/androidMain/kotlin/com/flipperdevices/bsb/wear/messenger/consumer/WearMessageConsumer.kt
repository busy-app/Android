package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.wear.messenger.message.DecodedWearMessage
import com.flipperdevices.bsb.wear.messenger.message.WearMessage
import kotlinx.coroutines.flow.Flow

interface WearMessageConsumer {
    val messagesFlow: Flow<DecodedWearMessage<*>>
    fun <T> consume(message: WearMessage<T>, byteArray: ByteArray)
}