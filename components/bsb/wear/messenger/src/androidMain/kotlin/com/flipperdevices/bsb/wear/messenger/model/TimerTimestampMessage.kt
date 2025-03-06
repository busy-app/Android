package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.message.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.message.WearMessage

object TimerTimestampMessage : WearMessage<TimerTimestamp> by JsonWearMessage(
    json = JsonWearMessage.DEFAULT_JSON,
    path = "/wearsync/timer_timestamp"
)
