package com.flipperdevices.bsb.timer.background.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerAction
import com.flipperdevices.bsb.timer.background.model.TimerServiceState
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerService::class)
@Suppress("TooManyFunctions")
class TimerServiceImpl(
    private val timerApi: TimerApi
) : TimerService {
    private val scope = CoroutineScope(SupervisorJob() + FlipperDispatchers.default)
    private val _state = MutableStateFlow<TimerServiceState>(TimerServiceState.Pending)

    override val state: StateFlow<TimerServiceState> = _state.asStateFlow()

    private suspend fun initWork(timerSettings: TimerSettings) {
        timerApi.startTimer(TimerState(timerSettings.intervalsSettings.work))
        _state.update {
            TimerServiceState.Started(
                currentIteration = 0,
                maxIteration = timerSettings.totalIterations,
                timerState = timerApi.getState().filterNotNull().first(),
                timerSettings = timerSettings,
                status = TimerServiceState.Status.WORK
            )
        }
    }

    private suspend fun tryStartRest(state: TimerServiceState.Started): Boolean {
        if (!state.timerSettings.intervalsSettings.isEnabled) return false
        if (state.status != TimerServiceState.Status.WORK) return false
        if (state.currentIteration >= state.maxIteration) return false

        timerApi.startTimer(TimerState(state.timerSettings.intervalsSettings.rest))
        _state.update {
            state.copy(
                timerState = timerApi.getState().filterNotNull().first(),
                status = TimerServiceState.Status.REST
            )
        }
        return true
    }

    private suspend fun tryStartLongRest(state: TimerServiceState.Started): Boolean {
        if (!state.timerSettings.intervalsSettings.isEnabled) return false
        if (state.status != TimerServiceState.Status.WORK) return false
        if (state.currentIteration < state.maxIteration) return false
        timerApi.startTimer(TimerState(state.timerSettings.intervalsSettings.longRest))
        _state.update {
            state.copy(
                timerState = timerApi.getState().filterNotNull().first(),
                status = TimerServiceState.Status.LONG_REST
            )
        }
        return true
    }

    private suspend fun tryStartWork(state: TimerServiceState.Started): Boolean {
        if (state.status == TimerServiceState.Status.WORK) return false
        if (state.status == TimerServiceState.Status.LONG_REST) return false
        if (state.currentIteration >= state.maxIteration) return false
        timerApi.startTimer(TimerState(state.timerSettings.intervalsSettings.work))
        _state.update {
            state.copy(
                timerState = timerApi.getState().filterNotNull().first(),
                status = TimerServiceState.Status.WORK,
                currentIteration = state.currentIteration + 1
            )
        }
        return true
    }

    private suspend fun tryStartFinish(state: TimerServiceState.Started): Boolean {
        if (!state.timerSettings.intervalsSettings.isEnabled) {
            if (state.status != TimerServiceState.Status.WORK) return false
            if (state.currentIteration < state.maxIteration) return false
        } else {
            if (state.status != TimerServiceState.Status.LONG_REST) return false
            if (state.currentIteration < state.maxIteration) return false
        }
        timerApi.stopTimer()
        timerApi.getState().filter { it == null }.first()
        _state.emit(TimerServiceState.Finished)
        return true
    }

    private suspend fun skipUnsafe() {
        val currentState = state.first() as? TimerServiceState.Started ?: return

        if (tryStartRest(currentState)) {
            Unit
        } else if (tryStartLongRest(currentState)) {
            Unit
        } else if (tryStartWork(currentState)) {
            Unit
        } else if (tryStartFinish(currentState)) {
            Unit
        }
    }

    override fun skip() {
        scope.launch {
            skipUnsafe()
        }
    }

    override fun startWith(timerSettings: TimerSettings) {
        scope.launch {
            initWork(timerSettings)
        }
    }

    override fun togglePause() {
        timerApi.onAction(TimerAction.PAUSE)
    }

    override fun stop() {
        scope.launch {
            timerApi.stopTimer()
            timerApi.getState().filter { it == null }.first()
            _state.emit(TimerServiceState.Pending)
        }
    }

    private val ControlledTimerState?.isAlmostFinished: Boolean
        get() = (this?.timerState?.duration?.inWholeSeconds ?: 0L) <= 1

    private fun collectTimerState() {
        timerApi.getState()
            .onEach { timerState ->
                if (timerState.isAlmostFinished) {
                    scope.launch {
                        // wait for timer almost finish
                        delay(timeMillis = 900L)
                        skipUnsafe()
                    }
                }
                if (timerState == null) {
                    skipUnsafe()
                } else {
                    _state.update { state ->
                        (state as? TimerServiceState.Started)
                            ?.copy(timerState = timerState)
                            ?: state
                    }
                }
            }.launchIn(scope)
    }

    init {
        collectTimerState()
    }
}
