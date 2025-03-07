package com.flipperdevices.bsb.wear.messenger.producer

import android.util.Log
import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
) : WearMessageProducer {

    override suspend fun <T> produce(message: WearMessageSerializer<T>, value: T): Unit = coroutineScope {
        val nodes = wearDataLayerRegistry.nodeClient.connectedNodes.await()
        Log.d(TAG, "produce: found ${nodes.size} nodes")
        kotlin.runCatching {
            val byteArray = message.encode(value)
            nodes.map {
                async {
                    wearDataLayerRegistry.messageClient.sendMessage(
                        it.id,
                        message.path,
                        byteArray
                    )
                }
            }.awaitAll()
        }.onFailure {
            Log.e(TAG, "produce: failed to send message ${it.stackTraceToString()}")
        }.onSuccess {
            Log.d(TAG, "produce: message sent: ${message.path} $message")
        }
    }

    companion object {
        private const val TAG = "WearMessageProducer"
    }
}
