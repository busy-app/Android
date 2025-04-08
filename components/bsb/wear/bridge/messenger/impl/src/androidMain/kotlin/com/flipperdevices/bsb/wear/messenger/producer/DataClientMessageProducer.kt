package com.flipperdevices.bsb.wear.messenger.producer

import com.flipperdevices.bsb.wear.messenger.serializer.WearMessageSerializer
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataRequest
import kotlinx.coroutines.tasks.await
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageProducer::class)
class DataClientMessageProducer(
    private val dataClient: DataClient
) : WearMessageProducer, LogTagProvider {
    override val TAG: String = "DataClientMessageProducer"

    override suspend fun <T> produce(
        message: WearMessageSerializer<T>,
        value: T
    ) {
        info { "Try to send: ${message.path} $message" }
        val byteArray = message.encode(value)
        val request = PutDataRequest.create(message.path).apply {
            data = byteArray
        }
        runCatching {
            dataClient.putDataItem(request).await()
        }.onFailure { throwable ->
            error(throwable) { "#produce failed to send message ${message.path}" }
        }.onSuccess { messageId ->
            info { "#produce message sent: ${message.path} with id $messageId" }
        }
    }
}
