package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.metronome.api.MetronomeApi
import com.flipperdevices.bsb.timer.background.api.delegates.CompositeTimerStateListener
import com.flipperdevices.bsb.timer.background.api.delegates.TimerLoopJob
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerAction
import com.flipperdevices.bsb.timer.background.model.toPublicState
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class CommonTimerApi(
    private val scope: CoroutineScope,
    private val compositeListeners: CompositeTimerStateListener,
    private val metronomeApi: MetronomeApi
) : TimerApi, LogTagProvider {
    override val TAG = "CommonTimerApi"

    private val mutex = Mutex()
    private val state = MutableStateFlow<ControlledTimerState?>(null)

    private var timerJob: TimerLoopJob? = null
    private var stateInvalidateJob: Job? = null

    fun addListener(listener: TimerStateListener) {
        compositeListeners.addListener(listener)
    }

    fun removeListener(listener: TimerStateListener) {
        compositeListeners.removeListener(listener)
    }

    override fun startTimer(initialTimerState: TimerState) {
        scope.launch {
            withLock(mutex, "start") {
                stateInvalidateJob?.cancelAndJoin()
                timerJob?.cancelAndJoin()
                val timer = TimerLoopJob(scope, Clock.System.now(), initialTimerState)
                timerJob = timer
                compositeListeners.onTimerStart()
                stateInvalidateJob = timer.getInternalState()
                    .onEach { internalState ->
                        if (internalState.timerState.minute <= 0 && internalState.timerState.second <= 0) {
                            stopSelf()
                        } else {
                            state.emit(internalState.toPublicState())
                            metronomeApi.play()
                        }
                    }.launchIn(scope)
            }
        }
    }

    override fun getState() = state.asStateFlow()

    override fun onAction(action: TimerAction) {
        scope.launch {
            withLock(mutex, "action") {
                timerJob?.onAction(action)
            }
        }
    }

    override fun stopTimer() {
        scope.launch {
            stopSelf()
        }
    }

    private suspend fun stopSelf() {
        withLock(mutex, "stop") {
            withContext(NonCancellable) {
                stateInvalidateJob?.cancel()
                timerJob?.cancelAndJoin()
                timerJob = null
                stateInvalidateJob = null
                state.emit(null)
                compositeListeners.onTimerStop()
            }
        }
    }
}
