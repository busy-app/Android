package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage
import com.flipperdevices.core.log.info

suspend fun WearMessageProducer.produce(message: WearMessage) {
    info { "Produce $message" }
    when (message) {
        TimerTimestampRequestMessage -> produce(
            message = TimerTimestampRequestMessage.serializer,
            value = Unit
        )

        is TimerTimestampMessage -> produce(
            message = TimerTimestampMessage.serializer,
            value = message.instance
        )
    }
}
