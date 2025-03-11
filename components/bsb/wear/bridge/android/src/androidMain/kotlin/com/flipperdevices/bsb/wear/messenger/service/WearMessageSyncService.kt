package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.timer.background.model.compareAndGetState
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WearMessageSyncService : LogTagProvider {
    override val TAG = "TimerForegroundService"
    private val wearSyncComponent by lazy {
        ComponentHolder.component<WearSyncComponent>()
    }

    private val wearDataLayerModule by lazy {
        ComponentHolder.component<WearDataLayerModule>()
    }
    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)

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

                    PongMessage,
                    PingMessage,
                    is AppBlockerCountMessage,
                    is TimerSettingsMessage -> Unit

                    is TimerTimestampMessage -> {
                        val old = wearSyncComponent.timerApi
                            .getTimestampState()
                            .first()
                        val resolved = old.compareAndGetState(message.instance)
                        if (old == resolved) {
                            sendTimerTimestampMessage()
                            return@onEach
                        }
                        wearSyncComponent.timerApi.setTimestampState(resolved)
                    }
                }
            }.launchIn(scope)
    }

    fun onCreate() {
        info { "#onCreate" }
        scope.launch {
            startMessageJob()
            startSettingsChangeJob()
            startAppBlockerCountChangeJob()
            startClientConnectJob()
            startStateChangeJob()
        }
    }

    fun onDestroy() {
        info { "#onDestroy" }
        scope.cancel()
    }
}
