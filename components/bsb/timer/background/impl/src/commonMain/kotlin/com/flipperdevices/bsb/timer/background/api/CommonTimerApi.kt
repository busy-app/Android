package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.metronome.api.MetronomeApi
import com.flipperdevices.bsb.timer.background.api.delegates.CompositeTimerStateListener
import com.flipperdevices.bsb.timer.background.api.delegates.TimerLoopJob
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.PauseData
import com.flipperdevices.bsb.timer.background.model.PauseType
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.model.isLastIteration
import com.flipperdevices.bsb.timer.background.util.updateState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    private val timerStateFlow = MutableStateFlow<ControlledTimerState>(ControlledTimerState.NotStarted)
    private val timerTimestampFlow = MutableStateFlow<TimerTimestamp?>(null)

    private var timerJob: TimerLoopJob? = null
    private var stateInvalidateJob: Job? = null

    fun addListener(listener: TimerStateListener) {
        compositeListeners.addListener(listener)
    }

    fun removeListener(listener: TimerStateListener) {
        compositeListeners.removeListener(listener)
    }

    override fun setTimestampState(state: TimerTimestamp?) {
        scope.launch {
            if (state == null) {
                stopSelf()
                return@launch
            }
            withLock(mutex, "start") {
                stateInvalidateJob?.cancelAndJoin()
                timerJob?.cancelAndJoin()
                timerTimestampFlow.emit(state)
                val timer = TimerLoopJob(scope, state)
                timerJob = timer
                compositeListeners.onTimerStart()
                stateInvalidateJob = timer.getInternalState()
                    .onEach { internalState ->
                        timerStateFlow.emit(internalState)
                        when (internalState) {
                            ControlledTimerState.NotStarted -> {
                                stopSelf()
                            }

                            ControlledTimerState.Finished -> Unit

                            is ControlledTimerState.Running -> {
                                if (internalState.pauseType != null) {
                                    metronomeApi.play()
                                }
                            }
                        }
                    }.launchIn(scope)
            }
        }
    }

    override fun getTimestampState(): StateFlow<TimerTimestamp?> {
        return timerTimestampFlow.asStateFlow()
    }

    override fun getState() = timerStateFlow.asStateFlow()

    private suspend fun stopSelf() {
        withLock(mutex, "stop") {
            withContext(NonCancellable) {
                timerTimestampFlow.value = null
                stateInvalidateJob?.cancel()
                timerJob?.cancelAndJoin()
                timerJob = null
                stateInvalidateJob = null
                timerStateFlow.emit(ControlledTimerState.NotStarted)
                compositeListeners.onTimerStop()
            }
        }
    }

    enum class EventType {
        NONE, PAUSE_AFTER_WORK, PAUSE_AFTER_REST
    }

    init {
        getState()
            .filterIsInstance<ControlledTimerState.Running>()
            .distinctUntilChangedBy { it.timeLeft.inWholeSeconds }
            .map { state ->
                if (state.timeLeft.inWholeSeconds != 0L) {
                    EventType.NONE
                } else {
                    when (state) {
                        is ControlledTimerState.Running.LongRest -> {
                            if (state.timerSettings.intervalsSettings.autoStartWork) {
                                EventType.NONE
                            } else if (state.isLastIteration) {
                                EventType.NONE
                            } else {
                                EventType.PAUSE_AFTER_REST
                            }
                        }

                        is ControlledTimerState.Running.Rest -> {
                            if (state.timerSettings.intervalsSettings.autoStartWork) {
                                EventType.NONE
                            } else {
                                EventType.PAUSE_AFTER_REST
                            }
                        }

                        is ControlledTimerState.Running.Work -> {
                            if (state.timerSettings.intervalsSettings.autoStartRest) {
                                EventType.NONE
                            } else {
                                EventType.PAUSE_AFTER_WORK
                            }
                        }
                    }
                }
            }
            .onEach {
                val type = when (it) {
                    EventType.PAUSE_AFTER_WORK -> PauseType.AFTER_WORK
                    EventType.PAUSE_AFTER_REST -> PauseType.AFTER_REST
                    EventType.NONE -> return@onEach
                }
                updateState { state ->
                    state ?: return@updateState state
                    if (state.pauseData != null) return@updateState state
                    state.copy(
                        pauseData = PauseData(
                            type = type,
                            instant = Clock.System.now()
                        )
                    )
                }
            }
            .launchIn(scope)
    }
}
