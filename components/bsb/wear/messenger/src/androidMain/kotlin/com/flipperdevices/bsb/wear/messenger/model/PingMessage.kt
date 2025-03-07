package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object PingMessage : WearMessage {
    val serializer get() = UnitWearMessageSerializer(path = "/wearsync/ping")
}
