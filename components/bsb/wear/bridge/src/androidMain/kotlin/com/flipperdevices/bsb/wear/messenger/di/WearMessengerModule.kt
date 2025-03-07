package com.flipperdevices.bsb.wear.messenger.di

import android.content.Context
import com.flipperdevices.bsb.wear.messenger.consumer.WearDataLayerRegistryMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.producer.WearDataLayerRegistryMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.core.di.AppGraph
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class WearMessengerModule(
    context: Context,
    scope: CoroutineScope
) {
    @OptIn(ExperimentalHorologistApi::class)
    private val wearDataLayerRegistry by lazy {
        WearDataLayerRegistry.fromContext(
            application = context,
            coroutineScope = scope
        )
    }

    @OptIn(ExperimentalHorologistApi::class)
    private val messageClient by lazy {
        wearDataLayerRegistry.messageClient
    }

    @OptIn(ExperimentalHorologistApi::class)
    val wearMessageProducer: WearMessageProducer by lazy {
        WearDataLayerRegistryMessageProducer(
            wearDataLayerRegistry = wearDataLayerRegistry,
            messageClient = messageClient
        )
    }

    @OptIn(ExperimentalHorologistApi::class)
    val wearMessageConsumer: WearMessageConsumer by lazy {
        WearDataLayerRegistryMessageConsumer(
            wearDataLayerRegistry = wearDataLayerRegistry,
            messageClient = messageClient
        )
    }
}
