package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer

class TimerTimestampMessage(val instance: TimerTimestamp?) : WearMessage {
    companion object {
        val serializer
            get() = JsonWearMessage<TimerTimestamp?>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_timestamp"
            )
    }
}
