package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object PongMessage : WearMessage {
    val serializer: UnitWearMessageSerializer
        get() = UnitWearMessageSerializer(path = "/wearsync/pong")
}
