package com.flipperdevices.bsb.wear.messenger.di

import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.core.di.AppGraph
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppGraph::class)
interface AndroidSyncComponent {
    val wearMessageConsumer: WearMessageConsumer
    val wearMessageProducer: WearMessageProducer
}
