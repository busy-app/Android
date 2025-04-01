package com.flipperdevices.bsb.timer.background.statefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationData
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationType
import com.flipperdevices.bsb.timer.background.statefactory.util.maxIterationCount
import com.flipperdevices.core.log.TaggedLogger
import kotlinx.datetime.Clock

internal val controlledTimerStateFactoryLogger = TaggedLogger("ControlledTimerStateFactory")

@Suppress("LongMethod")
fun TimerTimestamp.toState(): ControlledTimerState {
    if (this !is TimerTimestamp.Running) {
        return ControlledTimerState.NotStarted
    }
    val now = pause ?: Clock.System.now()

    if (settings.totalTime is TimerDuration.Infinite) {
        return toInfiniteState(now)
    }

    val iterationList = settings.buildIterationList()

    // Filter only data which is not yet started
    val iterationsDataLeft = iterationList
        .filter { data ->
            when (data) {
                is IterationData.Default -> {
                    now <= start.plus(data.startOffset).plus(data.duration)
                }

                is IterationData.Pending -> {
                    confirmNextStepClick < start.plus(data.startOffset)
                }
            }
        }
    val currentIterationData = iterationsDataLeft.firstOrNull()

    if (currentIterationData == null) {
        return ControlledTimerState.Finished(timerSettings = settings)
    }

    val iterationCountLeft = settings.maxIterationCount
        .minus(
            iterationsDataLeft
                .filterIndexed { i, _ -> i != 0 }
                .count { data -> data.iterationType == IterationType.WORK }
        )

    val currentIterationTypeTimeLeft = start
        .plus(currentIterationData.startOffset)
        .plus(currentIterationData.duration)
        .minus(now)

    return when (currentIterationData.iterationType) {
        IterationType.WORK -> ControlledTimerState.InProgress.Running.Work(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
            timePassed = now.minus(start)
        )

        IterationType.REST -> ControlledTimerState.InProgress.Running.Rest(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
            timePassed = now.minus(start)
        )

        IterationType.LONG_REST -> ControlledTimerState.InProgress.Running.LongRest(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
            timePassed = now.minus(start)
        )

        IterationType.WAIT_AFTER_REST -> ControlledTimerState.InProgress.Await(
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
            pausedAt = start.plus(currentIterationData.startOffset),
            type = ControlledTimerState.InProgress.AwaitType.AFTER_REST
        )

        IterationType.WAIT_AFTER_WORK -> ControlledTimerState.InProgress.Await(
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
            pausedAt = start.plus(currentIterationData.startOffset),
            type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
        )
    }
}
