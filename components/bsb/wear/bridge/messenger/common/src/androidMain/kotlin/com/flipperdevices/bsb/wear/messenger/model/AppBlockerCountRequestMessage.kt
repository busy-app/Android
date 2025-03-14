package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object AppBlockerCountRequestMessage : WearMessage {
    val serializer: UnitWearMessageSerializer
        get() = UnitWearMessageSerializer(path = "/wearsync/timer_blocked_app_count_request")
}
