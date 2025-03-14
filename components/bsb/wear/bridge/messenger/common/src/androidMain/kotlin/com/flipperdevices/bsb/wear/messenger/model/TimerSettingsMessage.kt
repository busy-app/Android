package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer

data class TimerSettingsMessage(val instance: TimerSettings) : WearMessage {
    companion object {
        val serializer: JsonWearMessageSerializer<TimerSettings>
            get() = JsonWearMessage<TimerSettings>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_settings"
            )
    }
}
