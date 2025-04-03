package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.log.LogTagProvider

interface WearMessageProducer : LogTagProvider {
    suspend fun <T> produce(message: WearMessageSerializer<T>, value: T)
}
