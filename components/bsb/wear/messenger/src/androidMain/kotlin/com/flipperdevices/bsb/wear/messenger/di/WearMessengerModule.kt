package com.flipperdevices.bsb.wear.messenger.di


import android.content.Context
import com.flipperdevices.bsb.wear.messenger.consumer.WearDataLayerRegistryMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.producer.WearDataLayerRegistryMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.CoroutineScope

interface WearMessengerModule {
    val wearMessageProducer: WearMessageProducer
    val wearMessageConsumer: WearMessageConsumer

    class Default(
        context: Context,
        coroutineScope: CoroutineScope,
    ) : WearMessengerModule {
        @OptIn(ExperimentalHorologistApi::class)
        private val wearDataLayerRegistry by lazy {
            WearDataLayerRegistry.fromContext(
                application = context,
                coroutineScope = coroutineScope
            )
        }

        @OptIn(ExperimentalHorologistApi::class)
        private val messageClient by lazy {
            wearDataLayerRegistry.messageClient
        }

        @OptIn(ExperimentalHorologistApi::class)
        override val wearMessageProducer: WearMessageProducer by lazy {
            WearDataLayerRegistryMessageProducer(
                wearDataLayerRegistry = wearDataLayerRegistry,
                messageClient = messageClient
            )
        }

        @OptIn(ExperimentalHorologistApi::class)
        override val wearMessageConsumer: WearMessageConsumer by lazy {
            WearDataLayerRegistryMessageConsumer(
                wearDataLayerRegistry = wearDataLayerRegistry,
                messageClient = messageClient
            )
        }
    }
}