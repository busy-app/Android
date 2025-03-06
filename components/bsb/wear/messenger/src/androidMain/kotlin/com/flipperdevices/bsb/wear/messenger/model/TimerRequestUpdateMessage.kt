package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.message.ByteWearMessage
import com.flipperdevices.bsb.wear.messenger.message.WearMessage

object TimerRequestUpdateMessage: WearMessage<Byte> by ByteWearMessage(path = "/wearsync/request_update")
