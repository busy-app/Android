package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer

data class WearOSTimerSettingsMessage(val instance: List<WearOSTimerSettings>) {
    companion object {
        val serializer: JsonWearMessageSerializer<List<WearOSTimerSettings>>
            get() = JsonWearMessage<List<WearOSTimerSettings>>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_settings_wearos"
            )
    }
}
