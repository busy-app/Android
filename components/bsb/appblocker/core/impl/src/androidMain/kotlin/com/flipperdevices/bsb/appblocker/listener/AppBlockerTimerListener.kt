package com.flipperdevices.bsb.appblocker.listener

import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class AppBlockerTimerListener(
    private val appBlockerApi: AppBlockerApi,
    private val looperFactory: () -> UsageStatsLooper,
    private val scope: CoroutineScope,
    private val timerApi: TimerApi
) : TimerStateListener, LogTagProvider {
    override val TAG = "AppBlockerTimer"
    private var looper: UsageStatsLooper? = null
    private var timerStateListenerJob: Job? = null

    override fun onTimerStart(timerSettings: TimerSettings) {
        timerStateListenerJob?.cancel()
        timerStateListenerJob = combine(
            timerApi.getState(),
            appBlockerApi.isAppBlockerSupportActive()
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
                ControlledTimerState.Finished,
                ControlledTimerState.NotStarted -> false
            }
        }.stateIn(scope, SharingStarted.WhileSubscribed(), false)
            .onEach { isBlockActive ->
                if (isBlockActive) {
                    startLoop()
                } else {
                    stopLoop()
                }
            }.launchIn(scope)
    }

    override fun onTimerStop() {
        stopLoop()
        timerStateListenerJob?.cancel()
    }

    private fun startLoop() {
        info { "Start usage stats looper for app blocker" }
        val nonNullLooper = looper ?: looperFactory().also { looper = it }
        nonNullLooper.startLoop()
    }

    private fun stopLoop() {
        info { "Try to stop looper $looper" }
        looper?.stopLoop()
    }
}
