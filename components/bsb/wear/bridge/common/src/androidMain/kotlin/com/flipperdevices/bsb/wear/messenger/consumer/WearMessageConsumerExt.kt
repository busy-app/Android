package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

val WearMessageConsumer.bMessageFlow: Flow<WearMessage>
    get() = messagesFlow
        .mapNotNull { decodedWearMessage ->
            when (decodedWearMessage.path) {
                PingMessage.serializer.path -> PingMessage
                PongMessage.serializer.path -> PongMessage
                TimerTimestampRequestMessage.serializer.path -> TimerTimestampRequestMessage
                TimerTimestampMessage.serializer.path -> TimerTimestampMessage(
                    decodedWearMessage.value as? TimerTimestamp
                )

                TimerActionMessage.Finish.serializer.path -> TimerActionMessage.Finish
                TimerActionMessage.ConfirmNextStage.serializer.path -> TimerActionMessage.ConfirmNextStage
                TimerActionMessage.Pause.serializer.path -> TimerActionMessage.Pause
                TimerActionMessage.Restart.serializer.path -> TimerActionMessage.Restart
                TimerActionMessage.Resume.serializer.path -> TimerActionMessage.Resume
                TimerActionMessage.Skip.serializer.path -> TimerActionMessage.Skip
                TimerActionMessage.Stop.serializer.path -> TimerActionMessage.Stop

                AppBlockerCountMessage.serializer.path -> AppBlockerCountMessage(
                    decodedWearMessage.value as BlockedAppCount
                )

                AppBlockerCountRequestMessage.serializer.path -> AppBlockerCountRequestMessage

                TimerSettingsMessage.serializer.path -> TimerSettingsMessage(
                    decodedWearMessage.value as TimerSettings
                )

                TimerSettingsRequestMessage.serializer.path -> TimerSettingsRequestMessage

                else -> null
            }
        }
