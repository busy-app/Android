package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage

suspend fun WearMessageProducer.produce(message: WearMessage) {
    when (message) {
        TimerTimestampRequestMessage -> produce(
            message = TimerTimestampRequestMessage.serializer,
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

        TimerSettingsRequestMessage -> produce(
            message = TimerSettingsRequestMessage.serializer,
            value = Unit
        )

        is AppBlockerCountMessage -> produce(
            message = AppBlockerCountMessage.serializer,
            value = message.instance
        )

        AppBlockerCountRequestMessage -> produce(
            message = AppBlockerCountRequestMessage.serializer,
            value = Unit
        )
    }
}
