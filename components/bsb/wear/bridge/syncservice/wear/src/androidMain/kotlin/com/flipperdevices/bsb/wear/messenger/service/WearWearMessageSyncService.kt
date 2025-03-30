package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
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
import com.flipperdevices.core.vibrator.api.BVibratorApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
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
    vibratorProvider: KIProvider<BVibratorApi>,
) : WearMessageSyncService {
    override val TAG = "WearWearMessageSyncService"

    private val timerApi by timerApiProvider
    private val wearConnectionApi by wearConnectionApiProvider

    private val wearMessageConsumer by wearMessageConsumerProvider
    private val wearMessageProducer by wearMessageProducerProvider
    private val vibrator by vibratorProvider

    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage() {
        val timerTimestamp = timerApi
            .getTimestampState()
            .first()
        val message = TimerTimestampMessage(timerTimestamp)
        wearMessageProducer.produce(message)
    }

    private fun startAndGetStateChangeJob(): Job {
        return timerApi.getTimestampState()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    private fun startAndGetClientConnectJob(): Job {
        return wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    @Suppress("MagicNumber")
    private fun startAndGetIntervalEndVibratorJob(): Job {
        return timerApi
            .getState()
            .distinctUntilChangedBy { state -> state::class }
            .map {
                when (it) {
                    is ControlledTimerState.Finished -> 10
                    is ControlledTimerState.InProgress.Await -> 10
                    is ControlledTimerState.InProgress.Running.LongRest -> 0
                    is ControlledTimerState.InProgress.Running.Rest -> 0
                    is ControlledTimerState.InProgress.Running.Work -> 0
                    ControlledTimerState.NotStarted -> -1
                }
            }
            .overflowChunked(2)
            .filter { (was, now) -> now >= was }
            .onEach { vibrator.vibrateOnce(VIBRATOR_DURATION) }
            .launchIn(scope)
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

                    TimerSettingsRequestMessage,
                    is TimerSettingsMessage -> Unit

                    is TimerTimestampMessage -> {
                        val old = timerApi
                            .getTimestampState()
                            .first()
                        if (old.lastSync > message.instance.lastSync) {
                            sendTimerTimestampMessage()
                        } else if (old.lastSync < message.instance.lastSync) {
                            timerApi.setTimestampState(message.instance)
                        }
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
                jobs.add(startAndGetIntervalEndVibratorJob())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.map { job -> job.cancel() }
        jobs.clear()
        scope.cancel()
    }

    companion object {
        private val VIBRATOR_DURATION = 500.milliseconds
    }
}
