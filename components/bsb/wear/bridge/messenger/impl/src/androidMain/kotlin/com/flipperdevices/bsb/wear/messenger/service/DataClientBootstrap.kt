package com.flipperdevices.bsb.wear.messenger.service

import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.util.toMessage
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class DataClientBootstrap(
    private val scope: CoroutineScope,
    private val dataClient: DataClient,
    private val wearMessageConsumer: WearMessageConsumer
) : LogTagProvider {
    override val TAG: String = "DataClientBootstrap"

    fun onCreate() {
        scope.launch {
            val dataItems: List<DataItem> = dataClient.dataItems.await().toList()
            info { "#onCreate gor ${dataItems.size} dataItems" }

            dataItems.onEach { dataItem ->
                val message = dataItem.toMessage() ?: return@onEach
                val byteArray = dataItem.data ?: run {
                    error { "#onDataChanged got empty data for ${dataItem.uri}" }
                    return@onEach
                }
                runCatching {
                    wearMessageConsumer.consume(
                        message = message,
                        byteArray = byteArray
                    )
                }.onFailure { error(it) { "#onDataChanged could not consume message" } }
            }
        }
    }
}