package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object TimerTimestampRequestMessage : WearMessage {
    val serializer = UnitWearMessageSerializer(path = "/wearsync/request_update")
}