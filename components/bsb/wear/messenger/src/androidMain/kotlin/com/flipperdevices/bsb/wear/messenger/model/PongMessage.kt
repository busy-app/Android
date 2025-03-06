package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.message.ByteWearMessage
import com.flipperdevices.bsb.wear.messenger.message.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.message.WearMessage

object PongMessage : WearMessage<Byte> by ByteWearMessage(path = "/wearsync/pong")
