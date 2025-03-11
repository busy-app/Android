package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.timer.background.api.delegates.CompositeTimerStateListener
import com.flipperdevices.bsb.timer.background.api.delegates.TimerLoopJob
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class CommonTimerApi(
    private val scope: CoroutineScope,
    private val compositeListeners: CompositeTimerStateListener
) : TimerApi, LogTagProvider {
    override val TAG = "CommonTimerApi"

    private val mutex = Mutex()
    private val timerStateFlow =
        MutableStateFlow<ControlledTimerState>(ControlledTimerState.NotStarted)
    private val timerTimestampFlow = MutableStateFlow<TimerTimestamp>(TimerTimestamp.Pending.NotStarted)

    private var timerJob: TimerLoopJob? = null
    private var stateInvalidateJob: Job? = null

    fun addListener(listener: TimerStateListener) {
        compositeListeners.addListener(listener)
    }

    fun removeListener(listener: TimerStateListener) {
        compositeListeners.removeListener(listener)
    }

    override fun setTimestampState(state: TimerTimestamp) {
        scope.launch {
            if (state !is TimerTimestamp.Running) {
                stopSelf()
                return@launch
            }
            withLock(mutex, "start") {
                stateInvalidateJob?.cancelAndJoin()
                timerJob?.cancelAndJoin()
                timerTimestampFlow.emit(state)
                val timer = TimerLoopJob(scope, state)
                timerJob = timer
                compositeListeners.onTimerStart(state.settings)
                stateInvalidateJob = timer.getInternalState()
                    .onEach { internalState ->
                        timerStateFlow.emit(internalState)
                        when (internalState) {
                            ControlledTimerState.NotStarted -> {
                                stopSelf()
                            }

                            is ControlledTimerState.InProgress.Await,
                            is ControlledTimerState.InProgress.Running,
                            ControlledTimerState.Finished -> Unit
                        }
                    }.launchIn(scope)
            }
        }
    }

    override fun getTimestampState(): StateFlow<TimerTimestamp> {
        return timerTimestampFlow.asStateFlow()
    }

    override fun getState() = timerStateFlow.asStateFlow()

    private suspend fun stopSelf() {
        withLock(mutex, "stop") {
            withContext(NonCancellable) {
                timerTimestampFlow.value = TimerTimestamp.Pending.Finished
                stateInvalidateJob?.cancel()
                timerJob?.cancelAndJoin()
                timerJob = null
                stateInvalidateJob = null
                timerStateFlow.emit(ControlledTimerState.NotStarted)
                compositeListeners.onTimerStop()
            }
        }
    }
}
