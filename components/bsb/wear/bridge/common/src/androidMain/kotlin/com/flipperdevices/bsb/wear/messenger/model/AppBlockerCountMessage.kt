package com.flipperdevices.bsb.wear.messenger.model

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer

data class AppBlockerCountMessage(val instance: BlockedAppCount) : WearMessage {
    companion object {
        val serializer: JsonWearMessageSerializer<BlockedAppCount>
            get() = JsonWearMessage<BlockedAppCount>(
                json = JsonWearMessageSerializer.DEFAULT_JSON,
                path = "/wearsync/timer_blocked_app_count"
            )
    }
}
