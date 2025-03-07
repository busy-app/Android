package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer

interface WearMessageProducer {
    suspend fun <T> produce(message: WearMessageSerializer<T>, value: T)
}
