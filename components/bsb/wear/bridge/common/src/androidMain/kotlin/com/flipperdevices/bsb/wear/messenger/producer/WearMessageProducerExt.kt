package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage

suspend fun WearMessageProducer.produce(message: WearMessage) {
    when (message) {
        PingMessage -> produce(
            message = PingMessage.serializer,
            value = Unit
        )

        PongMessage -> produce(
            message = PongMessage.serializer,
            value = Unit
        )

        is TimerActionMessage -> produce(
            message = message.serializer,
            value = message.value
        )

        TimerTimestampMessage.Request -> produce(
            message = TimerTimestampMessage.Request.serializer,
            value = Unit
        )

        is TimerTimestampMessage -> produce(
            message = TimerTimestampMessage.serializer,
            value = message.instance
        )

        is TimerSettingsMessage -> produce(
            message = TimerSettingsMessage.serializer,
            value = message.instance
        )
        TimerSettingsMessage.Request -> produce(
            message = TimerSettingsMessage.Request.serializer,
            value = Unit
        )
    }
}
