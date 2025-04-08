package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.krate.CloudWearOSTimerSettingsKrate
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.ktx.common.pmap
import com.flipperdevices.core.log.info
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageSyncService::class)
@Suppress("LongParameterList")
class AndroidWearMessageSyncService(
    timerApiProvider: KIProvider<TimerApi>,
    timerSettingsApiProvider: KIProvider<TimerSettingsApi>,
    appBlockerApiProvider: KIProvider<CardAppBlockerApi>,
    wearConnectionApiProvider: KIProvider<WearConnectionApi>,
    wearMessageConsumerProvider: KIProvider<WearMessageConsumer>,
    wearMessageProducerProvider: KIProvider<WearMessageProducer>,
    private val cloudWearOSTimerSettingsKrateProvider: KIProvider<CloudWearOSTimerSettingsKrate>
) : WearMessageSyncService {
    override val TAG = "AndroidWearMessageSyncService"

    private val timerApi by timerApiProvider
    private val timerSettingsApi by timerSettingsApiProvider
    private val appBlockerApi by appBlockerApiProvider
    private val wearConnectionApi by wearConnectionApiProvider

    private val wearMessageConsumer by wearMessageConsumerProvider
    private val wearMessageProducer by wearMessageProducerProvider

    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val jobs = mutableListOf<Job>()
    private val mutex = Mutex()

    private suspend fun sendTimerTimestampMessage(
        timerTimestamp: TimerTimestamp? = null
    ) {
        val timerTimestampNonNull = timerTimestamp ?: timerApi.getTimestampState().first()
        val message = TimerTimestampMessage(timerTimestampNonNull)
        wearMessageProducer.produce(message)
    }

    private suspend fun sendTimerSettingsMessage(
        timerSettingsList: List<WearOSTimerSettings>? = null
    ) {
        val settings = timerSettingsList ?: timerSettingsApi.getTimerSettingsListFlow().first()
            .pmap { settings ->
                WearOSTimerSettings(
                    instance = settings,
                    blockedAppCount = appBlockerApi.getBlockedAppCount(settings.id).first()
                )
            }
        cloudWearOSTimerSettingsKrateProvider.invoke().save(settings.toImmutableList())
        val message = TimerSettingsMessage(settings)
        wearMessageProducer.produce(message)
    }

    private fun startSettingsChangeJob(): Job {
        return timerSettingsApi.getTimerSettingsListFlow()
            .flatMapLatest { settingsList ->
                combine(
                    settingsList.map { settings ->
                        appBlockerApi.getBlockedAppCount(settings.id)
                            .map { blockedAppCount ->
                                WearOSTimerSettings(
                                    instance = settings,
                                    blockedAppCount = blockedAppCount
                                )
                            }
                    }
                ) {
                    it.toList()
                }
            }
            .debounce(timeout = 1.seconds)
            .onEach { sendTimerSettingsMessage(it) }
            .launchIn(scope)
    }

    private fun startStateChangeJob(): Job {
        return timerApi.getTimestampState()
            .onEach { sendTimerTimestampMessage(it) }
            .launchIn(scope)
    }

    private fun startClientConnectJob(): Job {
        return wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach {
                sendTimerTimestampMessage()
                sendTimerSettingsMessage()
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

                    is TimerSettingsMessage -> Unit

                    is TimerTimestampMessage -> {
                        val old = timerApi
                            .getTimestampState()
                            .first()
                        if (old.lastSync > message.instance.lastSync) {
                            info { "Received older timestamp state, so refresh on wearos" }
                            sendTimerTimestampMessage(old)
                        } else if (old.lastSync < message.instance.lastSync) {
                            info { "Received newer timestamp, so start timer state on android" }
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
