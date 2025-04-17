package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.bsb.wear.messenger.util.overflowChunked
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.warn
import com.flipperdevices.core.vibrator.api.BVibratorApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.milliseconds

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageSyncService::class)
class WearWearMessageSyncService(
    timerApiProvider: KIProvider<TimerApi>,
    wearConnectionApiProvider: KIProvider<WearConnectionApi>,
    wearMessageConsumerProvider: KIProvider<WearMessageConsumer>,
    wearMessageProducerProvider: KIProvider<WearMessageProducer>,
) : WearMessageSyncService {
    override val TAG = "WearWearMessageSyncService"

    private val timerApi by timerApiProvider
    private val wearConnectionApi by wearConnectionApiProvider

    private val wearMessageConsumer by wearMessageConsumerProvider
    private val wearMessageProducer by wearMessageProducerProvider

    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage(
        timerTimestamp: TimerTimestamp? = null
    ) {
        val timerTimestampNonNullable = timerTimestamp ?: timerApi
            .getTimestampState()
            .first()
        val message = TimerTimestampMessage(timerTimestampNonNullable)
        wearMessageProducer.produce(message)
    }

    private fun startAndGetStateChangeJob(): Job = scope.launch {
        timerApi.getTimestampState()
            .collectLatest { sendTimerTimestampMessage(it) }
    }

    private fun startAndGetClientConnectJob(): Job = scope.launch {
        wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .collectLatest { sendTimerTimestampMessage() }
    }

    private fun startAndGetMessageJob(): Job {
        return wearMessageConsumer
            .bMessageFlow
            .onEach { message ->
                info { "#startMessageJob got $message" }
                when (message) {
                    TimerTimestampRequestMessage -> {
                        sendTimerTimestampMessage()
                    }

                    is TimerTimestampMessage -> {
                        val old = timerApi
                            .getTimestampState()
                            .first()

                        if (old.lastSync > message.instance.lastSync) {
                            warn { "Received older timestamp state ${old.lastSync} > ${message.instance.lastSync}" }
                        } else if (old.lastSync < message.instance.lastSync) {
                            info { "Received newer timestamp state ${old.lastSync} < ${message.instance.lastSync}" }
                        }

                        timerApi.setTimestampState(message.instance)
                    }
                }
            }.launchIn(scope)
    }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            mutex.withLock {
                jobs.map { job -> async { job.cancelAndJoin() } }.awaitAll()
                jobs.add(startAndGetStateChangeJob())
                jobs.add(startAndGetMessageJob())
                jobs.add(startAndGetClientConnectJob())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.map { job -> job.cancel() }
        jobs.clear()
        scope.cancel()
    }
}
