package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object PingMessage : WearMessage {
    val serializer: UnitWearMessageSerializer
        get() = UnitWearMessageSerializer(path = "/wearsync/ping")
}
