package com.flipperdevices.bsb.appblocker.listener

import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerTimerListener(
    @Assisted private val timerApi: TimerApi,
    private val appBlockerApi: CardAppBlockerApi,
    private val looperFactory: (TimerSettingsId) -> UsageStatsLooper,
    private val scope: CoroutineScope,
) : TimerStateListener, LogTagProvider {
    override val TAG = "AppBlockerTimer"
    private val mutex = Mutex()

    private var looper: UsageStatsLooper? = null
    private var timerStateListenerJob: Job? = null

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        timerStateListenerJob?.cancel()
        timerStateListenerJob = combine(
            timerApi.getState(),
            appBlockerApi.isEnabled(timerSettings.id)
        ) { internalState, isAppBlockerSupportActive ->
            if (!isAppBlockerSupportActive) {
                return@combine false
            }
            return@combine when (internalState) {
                is ControlledTimerState.InProgress.Await -> when (internalState.type) {
                    ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> true
                    ControlledTimerState.InProgress.AwaitType.AFTER_REST -> false
                }

                is ControlledTimerState.InProgress.Running.Work -> internalState.isOnPause.not()
                is ControlledTimerState.InProgress.Running.LongRest,
                is ControlledTimerState.InProgress.Running.Rest,
                is ControlledTimerState.Finished,
                ControlledTimerState.NotStarted -> false
            }
        }.stateIn(scope, SharingStarted.WhileSubscribed(), false)
            .onEach { isBlockActive ->
                if (isBlockActive) {
                    startLoop(timerSettings.id)
                } else {
                    stopLoop()
                }
            }.launchIn(scope)
    }

    override suspend fun onTimerStop() {
        stopLoop()
        timerStateListenerJob?.cancel()
    }

    private suspend fun startLoop(timerSettingsId: TimerSettingsId) = mutex.withLock {
        info { "Start usage stats looper for app blocker" }
        val nonNullLooper = looper ?: looperFactory(timerSettingsId).also { looper = it }
        nonNullLooper.startLoop()
    }

    private suspend fun stopLoop() = mutex.withLock {
        info { "Try to stop looper $looper" }
        looper?.stopLoop()
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerStateListener.Factory::class, multibinding = true)
    class Factory(
        val factory: (
            timerApi: TimerApi
        ) -> AppBlockerTimerListener
    ) : TimerStateListener.Factory {
        override fun invoke(
            timerApi: TimerApi
        ) = factory(timerApi)
    }
}
