package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.timer.background.flow.TickFlow
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.statefactory.TimerStateFactory
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TimerLoopJob(
    scope: CoroutineScope,
    private val initialTimerTimestamp: TimerTimestamp,
    private val timerStateFactory: TimerStateFactory
) : LogTagProvider {
    override val TAG = "TimerLoopJob"

    private val timerStateFlow = MutableStateFlow(timerStateFactory.create(initialTimerTimestamp))

    internal fun getInternalState(): StateFlow<ControlledTimerState> = timerStateFlow.asStateFlow()

    private val job = scope.launch {
        TickFlow(duration = 20.milliseconds)
            .filter { initialTimerTimestamp.runningOrNull?.pause == null }
            .collectLatest {
                timerStateFlow.update { oldState ->
                    val newState = timerStateFactory.create(initialTimerTimestamp)
                    if (newState == oldState) {
                        oldState
                    } else {
                        newState
                    }
                }
            }
    }

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
    }
}
