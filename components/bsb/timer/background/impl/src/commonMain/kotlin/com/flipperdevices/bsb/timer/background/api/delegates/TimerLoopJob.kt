package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.timer.background.api.TimerTimestamp
import com.flipperdevices.bsb.timer.background.api.util.toState
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.isOnPause
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex

class TimerLoopJob(
    scope: CoroutineScope,
    private val initialTimerTimestamp: TimerTimestamp
) : LogTagProvider {
    override val TAG = "TimerLoopJob"

    private val timerStateFlow = MutableStateFlow(initialTimerTimestamp.toState())
    private val mutex = Mutex()

    internal fun getInternalState(): StateFlow<ControlledTimerState> = timerStateFlow.asStateFlow()

    private val job = TickFlow()
        .filter { !timerStateFlow.first().isOnPause }
        .onEach {
            withLock(mutex, "update") {
                timerStateFlow.emit(initialTimerTimestamp.toState())
            }
        }.launchIn(scope)

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
    }
}
