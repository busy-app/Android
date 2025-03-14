package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageProducer::class)
class WearDataLayerRegistryMessageProducer : WearMessageProducer {

    override suspend fun <T> produce(message: WearMessageSerializer<T>, value: T): Unit = Unit
}
