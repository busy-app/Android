package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.api.GmsWearConnectionApi
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.pmap
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.warn
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalHorologistApi::class)
@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageProducer::class)
class WearDataLayerRegistryMessageProducer(
    private val wearDataLayerRegistry: WearDataLayerRegistry,
    private val wearConnectionApi: GmsWearConnectionApi,
) : WearMessageProducer, LogTagProvider {
    override val TAG: String = "WearDataLayerRegistryMessageProducer"

    override suspend fun <T> produce(
        message: WearMessageSerializer<T>,
        value: T
    ) {
        info { "Try to send: ${message.path} $message" }
        runCatching {
            val statusNode = wearConnectionApi.statusFlow.first()

            val nodes = (statusNode as? GmsWearConnectionApi.GmsStatus.Connected)?.nodes
            if (nodes.isNullOrEmpty()) {
                error { "Can't send ${message.path} because nodes is empty" }
                return
            }
            val byteArray = message.encode(value)
            nodes.pmap { node ->
                runCatching {
                    wearDataLayerRegistry.messageClient.sendMessage(
                        node.id,
                        message.path,
                        byteArray
                    ).await()
                }.onFailure { throwable ->
                    error(throwable) { "#produce failed to send message to node $node" }
                }.onSuccess { messageId ->
                    info { "#produce message sent: ${message.path} with id $messageId to $node" }
                }
            }
        }
    }
}