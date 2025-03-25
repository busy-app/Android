package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer

data class TimerSettingsMessage(val instance: List<TimerSettings>) : WearMessage {
    companion object {
        val serializer: JsonWearMessageSerializer<List<TimerSettings>>
            get() = JsonWearMessage<List<TimerSettings>>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_settings_list"
            )
    }
}
