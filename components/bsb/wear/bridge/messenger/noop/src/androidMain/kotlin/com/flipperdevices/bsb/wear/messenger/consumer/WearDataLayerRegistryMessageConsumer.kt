package com.flipperdevices.bsb.wear.messenger.consumer

import com.flipperdevices.bsb.wear.messenger.serializer.DecodedWearMessage
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageConsumer::class)
class WearDataLayerRegistryMessageConsumer : WearMessageConsumer {
    override val messagesFlow: Flow<DecodedWearMessage<*>> = flowOf()

    override fun <T> consume(message: WearMessageSerializer<T>, byteArray: ByteArray) = Unit
}
