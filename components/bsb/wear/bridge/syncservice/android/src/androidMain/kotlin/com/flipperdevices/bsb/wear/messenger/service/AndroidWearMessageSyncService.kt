package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
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
class AndroidWearMessageSyncService(
    timerApiProvider: KIProvider<TimerApi>,
    timerSettingsApiProvider: KIProvider<TimerSettingsApi>,
    appBlockerFilterApiProvider: KIProvider<AppBlockerFilterApi>,
    wearConnectionApiProvider: KIProvider<WearConnectionApi>,
    wearMessageConsumerProvider: KIProvider<WearMessageConsumer>,
    wearMessageProducerProvider: KIProvider<WearMessageProducer>
) : WearMessageSyncService {
    override val TAG = "TimerForegroundService"

    private val timerApi by timerApiProvider
    private val timerSettingsApi by timerSettingsApiProvider
    private val appBlockerFilterApi by appBlockerFilterApiProvider
    private val wearConnectionApi by wearConnectionApiProvider

    private val wearMessageConsumer by wearMessageConsumerProvider
    private val wearMessageProducer by wearMessageProducerProvider

    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage() {
        val timerTimestamp = timerApi.getTimestampState().first()
        val message = TimerTimestampMessage(timerTimestamp)
        wearMessageProducer.produce(message)
    }

    private suspend fun sendTimerSettingsMessage(
        timerSettingsList: List<TimerSettings>? = null
    ) {
        val settings = timerSettingsList ?: timerSettingsApi.getTimerSettingsListFlow().first()
        val message = TimerSettingsMessage(settings)
        wearMessageProducer.produce(message)
    }

    private suspend fun sendAppBlockerCountMessage() {
        val appBlockerCount = appBlockerFilterApi.getBlockedAppCount().first()
        val message = AppBlockerCountMessage(appBlockerCount)
        wearMessageProducer.produce(message)
    }

    private fun startSettingsChangeJob(): Job {
        return timerSettingsApi.getTimerSettingsListFlow()
            .onEach {
                sendTimerSettingsMessage(timerSettingsList = it)
            }.launchIn(scope)
    }

    private fun startStateChangeJob(): Job {
        return timerApi.getTimestampState()
            .onEach { sendTimerTimestampMessage() }
            .launchIn(scope)
    }

    private fun startAppBlockerCountChangeJob(): Job {
        return appBlockerFilterApi.getBlockedAppCount()
            .onEach { sendAppBlockerCountMessage() }
            .launchIn(scope)
    }

    private fun startClientConnectJob(): Job {
        return wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach {
                sendTimerTimestampMessage()
                sendTimerSettingsMessage()
                sendAppBlockerCountMessage()
            }.launchIn(scope)
    }

    private fun startMessageJob(): Job {
        return wearMessageConsumer
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
                jobs.add(startMessageJob())
                jobs.add(startSettingsChangeJob())
                jobs.add(startAppBlockerCountChangeJob())
                jobs.add(startClientConnectJob())
                jobs.add(startStateChangeJob())
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
