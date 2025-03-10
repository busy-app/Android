package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

object TimerSettingsRequestMessage : WearMessage {
    val serializer = UnitWearMessageSerializer(path = "/wearsync/timer_settings_request")
}
