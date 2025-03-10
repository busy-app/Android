package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer
import com.flipperdevices.bsb.wear.messenger.serializer.UnitWearMessageSerializer

class TimerSettingsMessage(val instance: TimerSettings) : WearMessage {
    companion object {
        val serializer
            get() = JsonWearMessage<TimerSettings>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_settings"
            )
    }

    object Request : WearMessage {
        val serializer = UnitWearMessageSerializer(path = "/wearsync/timer_settings_request")
    }
}
