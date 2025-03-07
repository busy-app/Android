package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerRequestUpdateMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer

interface WearMessageProducer {
    suspend fun <T> produce(message: WearMessageSerializer<T>, value: T)
}


suspend fun WearMessageProducer.produce(message: WearMessage) {
    when (message) {
        PingMessage -> produce(
            message = PingMessage.serializer,
            value = Unit
        )

        PongMessage -> produce(
            message = PingMessage.serializer,
            value = Unit
        )

        is TimerActionMessage -> produce(
            message = message.serializer,
            value = message.value
        )

        TimerRequestUpdateMessage -> produce(
            message = TimerRequestUpdateMessage.serializer,
            value = Unit
        )

        is TimerTimestampMessage -> produce(
            message = TimerRequestUpdateMessage.serializer,
            value = Unit
        )
    }
}