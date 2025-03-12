package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.LogTagProvider
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

class WearMessageSyncService : LogTagProvider {
    override val TAG = "TimerForegroundService"
    private val wearSyncComponent by lazy {
        ComponentHolder.component<WearSyncComponent>()
    }

    private val wearDataLayerModule by lazy {
        ComponentHolder.component<WearDataLayerModule>()
    }
    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage() {
        val timerTimestamp = wearSyncComponent.timerApi.getTimestampState().first()
        val message = TimerTimestampMessage(timerTimestamp)
        wearDataLayerModule.wearMessageProducer.produce(message)
    }

    private suspend fun sendTimerSettingsMessage() {
        val settings = wearSyncComponent.krateApi.timerSettingsKrate.loadAndGet()
        val message = TimerSettingsMessage(settings)
        wearDataLayerModule.wearMessageProducer.produce(message)
    }

    private suspend fun sendAppBlockerCountMessage() {
        val appBlockerCount = wearSyncComponent.appBlockerFilterApi.getBlockedAppCount().first()
        val message = AppBlockerCountMessage(appBlockerCount)
        wearDataLayerModule.wearMessageProducer.produce(message)
    }

    private fun startSettingsChangeJob(): Job {
        return wearSyncComponent.krateApi.timerSettingsKrate.flow
            .onEach { sendTimerSettingsMessage() }
            .launchIn(scope)
    }

    private fun startStateChangeJob(): Job {
        return wearSyncComponent.timerApi.getTimestampState()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    private fun startAppBlockerCountChangeJob(): Job {
        return wearSyncComponent.appBlockerFilterApi.getBlockedAppCount()
            .onEach { sendAppBlockerCountMessage() }
            .launchIn(scope)
    }

    private fun startClientConnectJob(): Job {
        return wearSyncComponent.wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach {
                sendTimerTimestampMessage()
                sendTimerSettingsMessage()
                sendAppBlockerCountMessage()
            }.launchIn(scope)
    }

    private fun startMessageJob(): Job {
        return wearDataLayerModule.wearMessageConsumer
            .bMessageFlow
            .onEach { message ->
                info { "#startMessageJob got $message" }
                when (message) {
                    TimerTimestampRequestMessage -> {
                        sendTimerTimestampMessage()
                    }

                    TimerSettingsRequestMessage -> {
                        sendTimerSettingsMessage()
                    }

                    AppBlockerCountRequestMessage -> {
                        sendAppBlockerCountMessage()
                    }

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

    fun onCreate() {
        info { "#onCreate" }
        scope.launch {
            mutex.withLock {
                jobs.map { job -> async { job.cancelAndJoin() } }.awaitAll()
                jobs.add(startMessageJob())
                jobs.add(startSettingsChangeJob())
                jobs.add(startAppBlockerCountChangeJob())
                jobs.add(startClientConnectJob())
                jobs.add(startStateChangeJob())
            }
        }
    }

    fun onDestroy() {
        info { "#onDestroy" }
        jobs.map { job -> job.cancel() }
        jobs.clear()
        scope.cancel()
    }
}
