package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.isOnPause
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration.Companion.seconds

class TimerLoopJob(
    scope: CoroutineScope,
    initialTimerState: ControlledTimerState
) : LogTagProvider {
    override val TAG = "TimerLoopJob"

    private val timerStateFlow = MutableStateFlow(initialTimerState)
    private val mutex = Mutex()

    internal fun getInternalState() = timerStateFlow.asStateFlow()

    private val job = TickFlow()
        .filter { !timerStateFlow.first().isOnPause }
        .filter { timerStateFlow.first() is ControlledTimerState.Running }
        .onEach {
            withLock(mutex, "update") {
                timerStateFlow.update { original ->
                    when (original) {
                        ControlledTimerState.Finished -> original
                        ControlledTimerState.NotStarted -> original
                        is ControlledTimerState.Running -> {
                            when (original) {
                                is ControlledTimerState.Running.LongRest -> original.copy(
                                    timeLeft = original.timeLeft.minus(1.seconds)
                                )
                                is ControlledTimerState.Running.Rest -> original.copy(
                                    timeLeft = original.timeLeft.minus(1.seconds)
                                )
                                is ControlledTimerState.Running.Work -> original.copy(
                                    timeLeft = original.timeLeft.minus(1.seconds)
                                )
                            }
                        }
                    }
                }
            }
        }.launchIn(scope)

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
    }
}
