package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalHorologistApi::class)
@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageProducer::class)
class WearDataLayerRegistryMessageProducer(
    private val wearDataLayerRegistry: WearDataLayerRegistry,
    private val wearConnectionApi: WearConnectionApi,

) : WearMessageProducer, LogTagProvider {
    override val TAG: String = "WearDataLayerRegistryMessageProducer"

    override suspend fun <T> produce(message: WearMessageSerializer<T>, value: T): Unit = supervisorScope {
        launch {
            val nodes = listOfNotNull(wearConnectionApi.statusFlow.first().nodeOrNull)
                .filter { node -> node.isNearby }
            runCatching {
                val byteArray = message.encode(value)
                nodes.map { node ->
                    async {
                        wearDataLayerRegistry.messageClient.sendMessage(
                            node.id,
                            message.path,
                            byteArray
                        )
                    }
                }.awaitAll()
            }.onFailure { throwable ->
                error(throwable) { "#produce failed to send message ${throwable.stackTraceToString()}" }
            }.onSuccess {
                info { "#produce message sent: ${message.path} $message" }
            }
        }
    }
}
