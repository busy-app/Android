package com.flipperdevices.bsb.timer.background.newstatefactory.state

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.DefaultIterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.RestrictedIterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object TimerStateFactory {

    private fun getCurrentIteration(
        now: Instant,
        start: Instant,
        confirmNextStepClick: Instant,
        iterations: List<IterationData>
    ): IterationData? = iterations.firstOrNull { data ->
        when (data) {
            is IterationData.Default -> {
                now <= start.plus(data.startOffset).plus(data.duration)
            }

            is IterationData.Pending -> {
                confirmNextStepClick < start.plus(data.startOffset)
            }
        }
    }

    fun create(timestamp: TimerTimestamp): ControlledTimerState {
        if (timestamp !is TimerTimestamp.Running) {
            return ControlledTimerState.NotStarted
        }
        val now = timestamp.pause ?: Clock.System.now()

        val defaultIterationBuilder = DefaultIterationBuilder()
        val restrictedIterationBuilder = RestrictedIterationBuilder(defaultIterationBuilder)
        val iterations = when (val localTotalTime = timestamp.settings.totalTime) {
            is TimerDuration.Infinite -> {
                defaultIterationBuilder.build(
                    timestamp.settings,
                    now.minus(timestamp.start)
                )
            }

            is TimerDuration.Finite -> {
                restrictedIterationBuilder.build(
                    timestamp.settings,
                    localTotalTime.instance
                )
            }
        }
        val currentIterationData = getCurrentIteration(
            now = now,
            start = timestamp.start,
            confirmNextStepClick = timestamp.confirmNextStepClick,
            iterations = iterations
        ) ?: return ControlledTimerState.Finished(timerSettings = timestamp.settings)
        val maxIterationCount = iterations.count { it.iterationType == IterationType.WORK }
        val currentIterationDataIndex = iterations
            .indexOf(currentIterationData)
            .minus(1)
            .coerceAtLeast(0)
        val currentIterationCount = iterations
            .subList(0, currentIterationDataIndex)
            .count { it.iterationType == IterationType.WORK }

        val currentIterationTypeTimeLeft = timestamp.start
            .plus(currentIterationData.startOffset)
            .plus(currentIterationData.duration)
            .minus(now)

        return when (currentIterationData.iterationType) {
            IterationType.WORK -> ControlledTimerState.InProgress.Running.Work(
                timeLeft = TimerDuration(currentIterationTypeTimeLeft),
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.REST -> ControlledTimerState.InProgress.Running.Rest(
                timeLeft = TimerDuration(currentIterationTypeTimeLeft),
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.LONG_REST -> ControlledTimerState.InProgress.Running.LongRest(
                timeLeft = TimerDuration(currentIterationTypeTimeLeft),
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.WAIT_AFTER_REST -> ControlledTimerState.InProgress.Await(
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                pausedAt = timestamp.start.plus(currentIterationData.startOffset),
                type = ControlledTimerState.InProgress.AwaitType.AFTER_REST
            )

            IterationType.WAIT_AFTER_WORK -> ControlledTimerState.InProgress.Await(
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                pausedAt = timestamp.start.plus(currentIterationData.startOffset),
                type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
            )
        }
    }
}
