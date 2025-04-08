package com.flipperdevices.bsb.wear.messenger.util

import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.core.log.error
import com.google.android.gms.wearable.DataItem

@Suppress("CyclomaticComplexMethod")
 fun DataItem.toMessage() = when (uri.path) {
    TimerTimestampRequestMessage.serializer.path -> TimerTimestampRequestMessage.serializer
    TimerTimestampMessage.Companion.serializer.path -> TimerTimestampMessage.Companion.serializer

    TimerSettingsMessage.serializer.path -> TimerSettingsMessage.serializer
    TimerSettingsRequestMessage.serializer.path -> TimerSettingsRequestMessage.serializer
    else -> {
        error { "#toMessage could not handle wear message ${uri.path}" }
        null
    }
}