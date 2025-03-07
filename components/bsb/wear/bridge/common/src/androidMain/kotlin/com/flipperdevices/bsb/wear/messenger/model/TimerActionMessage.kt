@file:Suppress("MagicNumber")

package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.wear.messenger.serializer.ByteWearMessageSerializer

sealed interface TimerActionMessage : WearMessage {
    val serializer: ByteWearMessageSerializer
    val value: Byte

    object Pause : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_pause",
        )
        override val value: Byte = 0
    }

    object Resume : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_resume",
        )
        override val value: Byte = 1
    }

    object Stop : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_stop",
        )
        override val value: Byte = 2
    }

    object Skip : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_skip",
        )
        override val value: Byte = 3
    }

    object Finish : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_finish",
        )
        override val value: Byte = 4
    }

    object Restart : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_restart",
        )
        override val value: Byte = 5
    }

    object ConfirmNextStage : TimerActionMessage {
        override val serializer = ByteWearMessageSerializer(
            path = "/wearsync/action_confirm_next_stage",
        )
        override val value: Byte = 6
    }
}
