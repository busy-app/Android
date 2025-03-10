package om.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.util.confirmNextStep
import com.flipperdevices.bsb.timer.background.util.pause
import com.flipperdevices.bsb.timer.background.util.resume
import com.flipperdevices.bsb.timer.background.util.skip
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.di.WearDataLayerModule
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsb.wear.messenger.model.PongMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
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

    private val mutex = Mutex()

    private fun startStateChangeJob(): Job {
        return wearSyncComponent.timerApi.getTimestampState()
            .onEach { timerTimestamp ->
                val message = TimerTimestampMessage(timerTimestamp)
                wearDataLayerModule.wearMessageProducer.produce(message)
            }.launchIn(scope)
    }

    private fun startSettingsChangeJob(): Job {
        return wearSyncComponent.krateApi.timerSettingsKrate.flow
            .onEach { settings ->
                val message = TimerSettingsMessage(settings)
                wearDataLayerModule.wearMessageProducer.produce(message)
            }.launchIn(scope)
    }

    private fun startAppBlockerCountChangeJob(): Job {
        return wearSyncComponent.appBlockerFilterApi.getBlockedAppCount()
            .onEach { appBlockerCount ->
                val message = AppBlockerCountMessage(appBlockerCount)
                wearDataLayerModule.wearMessageProducer.produce(message)
            }.launchIn(scope)
    }

    private fun startMessageJob(): Job {
        return wearDataLayerModule.wearMessageConsumer
            .bMessageFlow
            .onEach { message ->
                when (message) {
                    TimerActionMessage.ConfirmNextStage -> {
                        wearSyncComponent.timerApi.confirmNextStep()
                    }

                    TimerActionMessage.Finish -> {
                        wearSyncComponent.timerApi.stop()
                    }

                    TimerActionMessage.Pause -> {
                        wearSyncComponent.timerApi.pause()
                    }

                    TimerActionMessage.Restart -> {
                        val settings = wearSyncComponent.krateApi.timerSettingsKrate.loadAndGet()
                        val timerTimestamp = TimerTimestamp(settings = settings)
                        wearSyncComponent.timerApi.setTimestampState(timerTimestamp)
                    }

                    TimerActionMessage.Resume -> {
                        wearSyncComponent.timerApi.resume()
                    }

                    TimerActionMessage.Skip -> {
                        wearSyncComponent.timerApi.skip()
                    }

                    TimerActionMessage.Stop -> {
                        wearSyncComponent.timerApi.stop()
                    }

                    TimerTimestampRequestMessage -> {
                        val timerTimestamp = wearSyncComponent.timerApi.getTimestampState().first()
                        val message = TimerTimestampMessage(timerTimestamp)
                        wearDataLayerModule.wearMessageProducer.produce(message)
                    }

                    TimerSettingsRequestMessage -> {
                        val settings = wearSyncComponent.krateApi.timerSettingsKrate.loadAndGet()
                        val message = TimerSettingsMessage(settings)
                        wearDataLayerModule.wearMessageProducer.produce(message)
                    }

                    AppBlockerCountRequestMessage -> {
                        val appBlockerCount = wearSyncComponent.appBlockerFilterApi.getBlockedAppCount().first()
                        val message = AppBlockerCountMessage(appBlockerCount)
                        wearDataLayerModule.wearMessageProducer.produce(message)
                    }

                    PongMessage,
                    PingMessage,
                    is AppBlockerCountMessage,
                    is TimerSettingsMessage,
                    is TimerTimestampMessage -> Unit
                }
            }.launchIn(scope)
    }

    fun onCreate() {
        info { "#onCreate" }
        scope.launch {
            mutex.withLock {
                startStateChangeJob()
                startMessageJob()
                startSettingsChangeJob()
                startAppBlockerCountChangeJob()
            }
        }
    }

    fun onDestroy() {
        info { "#onDestroy" }
        scope.cancel()
    }
}
