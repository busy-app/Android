package com.flipperdevices.bsb.timer.background.statefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationData
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationType
import com.flipperdevices.bsb.timer.background.statefactory.util.getIterationTypeDuration
import com.flipperdevices.bsb.timer.background.statefactory.util.getTypeByIndex
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.seconds

@Suppress("CyclomaticComplexMethod", "LongMethod")
internal fun TimerTimestamp.Running.toInfiniteState(now: Instant): ControlledTimerState {
    if (!settings.intervalsSettings.isEnabled) {
        return ControlledTimerState.InProgress.Running.Work(
            timerSettings = settings,
            timeLeft = TimerDuration.Infinite,
            isOnPause = pause != null,
            currentIteration = 1,
            maxIterations = 1,
            timePassed = now - start
        ).also { println(it) }
    }

    val maxTimeToPass = Clock.System.now() - start
    var timePassed = 0.seconds
    var i = 0
    val iterations = buildList<IterationData> {
        while (timePassed < maxTimeToPass) {
            val type = IterationType.getTypeByIndex(i)
            val duration = settings.getIterationTypeDuration(
                type = type,
                timeLeft = INFINITE
            )

            if (!settings.intervalsSettings.autoStartRest && lastOrNull()?.iterationType == IterationType.WORK) {
                IterationData.Pending(
                    startOffset = timePassed,
                    duration = INFINITE,
                    iterationType = IterationType.WAIT_AFTER_WORK
                ).run(::add)
            }
            if (!settings.intervalsSettings.autoStartWork && lastOrNull()?.iterationType == IterationType.REST) {
                IterationData.Pending(
                    startOffset = timePassed,
                    duration = INFINITE,
                    iterationType = IterationType.WAIT_AFTER_REST
                ).run(::add)
            }
            if (!settings.intervalsSettings.autoStartWork && lastOrNull()?.iterationType == IterationType.LONG_REST) {
                IterationData.Pending(
                    startOffset = timePassed,
                    duration = INFINITE,
                    iterationType = IterationType.WAIT_AFTER_REST
                ).run(::add)
            }
            IterationData.Default(
                startOffset = timePassed,
                duration = duration,
                iterationType = type
            ).run(::add)
            timePassed += duration
            i++
        }
    }
    val iterationPassed = iterations.count { data -> data.iterationType == IterationType.WORK }

    val currentIteration = iterations.filter { data ->
        when (data) {
            is IterationData.Default -> {
                now <= start.plus(data.startOffset).plus(data.duration)
            }

            is IterationData.Pending -> {
                confirmNextStepClick < start.plus(data.startOffset)
            }
        }
    }.firstOrNull() ?: IterationData.Default(
        startOffset = 0.seconds,
        duration = settings.intervalsSettings.work,
        iterationType = IterationType.WORK
    )

    val currentIterationTypeTimeLeft = start
        .plus(currentIteration.startOffset)
        .plus(currentIteration.duration)
        .minus(now)

    return when (currentIteration.iterationType) {
        IterationType.WORK -> ControlledTimerState.InProgress.Running.Work(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationPassed,
            maxIterations = iterationPassed,
            timePassed = now.minus(start)
        )

        IterationType.REST -> ControlledTimerState.InProgress.Running.Rest(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationPassed,
            maxIterations = iterationPassed,
            timePassed = now.minus(start)
        )

        IterationType.LONG_REST -> ControlledTimerState.InProgress.Running.LongRest(
            timeLeft = TimerDuration(currentIterationTypeTimeLeft),
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationPassed,
            maxIterations = iterationPassed,
            timePassed = now.minus(start)
        )

        IterationType.WAIT_AFTER_WORK -> ControlledTimerState.InProgress.Await(
            timerSettings = settings,
            currentIteration = iterationPassed,
            maxIterations = iterationPassed,
            pausedAt = start.plus(currentIteration.startOffset),
            type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
        )

        IterationType.WAIT_AFTER_REST -> ControlledTimerState.InProgress.Await(
            timerSettings = settings,
            currentIteration = iterationPassed,
            maxIterations = iterationPassed,
            pausedAt = start.plus(currentIteration.startOffset),
            type = ControlledTimerState.InProgress.AwaitType.AFTER_REST
        )
    }
}
