package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.timer.background.flow.TickFlow
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.statefactory.TimerStateFactory
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

class TimerLoopJob(
    scope: CoroutineScope,
    private val initialTimerTimestamp: TimerTimestamp,
    private val timerStateFactory: TimerStateFactory
) : LogTagProvider {
    override val TAG = "TimerLoopJob"

    private val timerStateFlow = MutableStateFlow(timerStateFactory.create(initialTimerTimestamp))
    private val mutex = Mutex()

    internal fun getInternalState(): StateFlow<ControlledTimerState> = timerStateFlow.asStateFlow()

    private val job = TickFlow(duration = 1000.milliseconds)
        .filter { initialTimerTimestamp.runningOrNull?.pause == null }
        .onEach {
            mutex.withLock {
                timerStateFlow.update { oldState ->
                    val newState = timerStateFactory.create(initialTimerTimestamp)
                    if (newState == oldState) oldState
                    else newState
                }
            }
        }.launchIn(scope)

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
    }
}
