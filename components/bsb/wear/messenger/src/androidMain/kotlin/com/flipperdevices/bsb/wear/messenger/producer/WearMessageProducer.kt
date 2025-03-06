package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.message.WearMessage

interface WearMessageProducer {
    suspend fun <T> produce(message: WearMessage<T>, value: T)
}