package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.message.ByteWearMessage
import com.flipperdevices.bsb.wear.messenger.message.WearMessage

object TimerActionMessage {
    val Pause = ByteWearMessage(path = "/wearsync/action_pause")
    val Resume = ByteWearMessage(path = "/wearsync/action_resume")
    val Stop = ByteWearMessage(path = "/wearsync/action_stop")
    val Skip = ByteWearMessage(path = "/wearsync/action_skip")
    val Finish = ByteWearMessage(path = "/wearsync/action_finish")
    val Restart = ByteWearMessage(path = "/wearsync/action_restart")
    val ConfirmNextStage = ByteWearMessage(path = "/wearsync/action_confirm_next_stage")
}
