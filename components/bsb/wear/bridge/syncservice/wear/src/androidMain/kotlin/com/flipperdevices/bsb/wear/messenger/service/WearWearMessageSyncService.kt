package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.di.WearOsSyncComponent
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageSyncService::class)
class WearWearMessageSyncService : WearMessageSyncService {
    override val TAG = "WearWearMessageSyncService"
    private val wearSyncComponent by lazy {
        ComponentHolder.component<WearSyncComponent>()
    }
    private val wearOsSyncComponent by lazy {
        ComponentHolder.component<WearOsSyncComponent>()
    }

    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage() {
        val timerTimestamp = wearSyncComponent.timerApi
            .getTimestampState()
            .first()
        val message = TimerTimestampMessage(timerTimestamp)
        wearOsSyncComponent.wearMessageProducer.produce(message)
    }

    private fun startAndGetStateChangeJob(): Job {
        return wearSyncComponent.timerApi.getTimestampState()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    private fun startAndGetClientConnectJob(): Job {
        return wearSyncComponent.wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    private fun startAndGetMessageJob(): Job {
        return wearOsSyncComponent.wearMessageConsumer
            .bMessageFlow
            .onEach { message ->
                info { "#startMessageJob got $message" }
                when (message) {
                    TimerTimestampRequestMessage -> {
                        sendTimerTimestampMessage()
                    }

                    AppBlockerCountRequestMessage,
                    TimerSettingsRequestMessage,
                    is AppBlockerCountMessage,
                    is TimerSettingsMessage -> Unit

                    is TimerTimestampMessage -> {
                        val old = wearSyncComponent.timerApi
                            .getTimestampState()
                            .first()
                        if (old.lastSync > message.instance.lastSync) {
                            sendTimerTimestampMessage()
                        } else if (old.lastSync < message.instance.lastSync) {
                            wearSyncComponent.timerApi.setTimestampState(message.instance)
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
