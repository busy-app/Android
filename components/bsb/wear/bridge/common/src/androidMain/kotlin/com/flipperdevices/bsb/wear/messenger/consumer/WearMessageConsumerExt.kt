package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.WearMessage
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

private val logger = TaggedLogger("WearMessageConsumerExt")
val WearMessageConsumer.bMessageFlow: Flow<WearMessage>
    get() = messagesFlow
        .mapNotNull { decodedWearMessage ->
            when (decodedWearMessage.path) {
                TimerTimestampRequestMessage.serializer.path -> TimerTimestampRequestMessage
                TimerTimestampMessage.serializer.path -> TimerTimestampMessage(
                    decodedWearMessage.value as TimerTimestamp
                )

                AppBlockerCountMessage.serializer.path -> AppBlockerCountMessage(
                    decodedWearMessage.value as BlockedAppCount
                )

                AppBlockerCountRequestMessage.serializer.path -> AppBlockerCountRequestMessage

                TimerSettingsMessage.serializer.path -> TimerSettingsMessage(
                    decodedWearMessage.value as TimerSettings
                )

                TimerSettingsRequestMessage.serializer.path -> TimerSettingsRequestMessage

                else -> {
                    logger.error { "#bMessageFlow could not handle wear message ${decodedWearMessage.path}" }
                    null
                }
            }
        }
