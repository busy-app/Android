package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.timer.background.flow.TickFlow
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.newstatefactory.state.TimerStateFactory
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex

class TimerLoopJob(
    scope: CoroutineScope,
    private val initialTimerTimestamp: TimerTimestamp
) : LogTagProvider {
    override val TAG = "TimerLoopJob"

    private val timerStateFlow = MutableStateFlow(TimerStateFactory.create(initialTimerTimestamp))
    private val mutex = Mutex()

    internal fun getInternalState(): StateFlow<ControlledTimerState> = timerStateFlow.asStateFlow()

    private val job = TickFlow()
        .filter { initialTimerTimestamp.runningOrNull?.pause == null }
        .onEach {
            withLock(mutex, "update") {
                timerStateFlow.emit(TimerStateFactory.create(initialTimerTimestamp))
            }
        }.launchIn(scope)

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
    }
}
