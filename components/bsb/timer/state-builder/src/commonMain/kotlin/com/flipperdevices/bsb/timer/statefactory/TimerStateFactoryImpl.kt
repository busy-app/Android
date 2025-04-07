package com.flipperdevices.bsb.timer.statefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.statefactory.iteration.datetime.TimeProvider
import com.flipperdevices.bsb.timer.statefactory.iteration.impl.CoercedIterationBuilder
import com.flipperdevices.bsb.timer.statefactory.iteration.impl.DefaultIterationBuilder
import com.flipperdevices.bsb.timer.statefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.statefactory.iteration.model.IterationType
import com.flipperdevices.core.di.AppGraph
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateFactory::class)
class TimerStateFactoryImpl(
    private val timeProvider: TimeProvider
) : TimerStateFactory {

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

            is IterationData.Infinite -> true
        }
    }

    private fun getIterations(
        timestamp: TimerTimestamp.Running,
        now: Instant
    ): List<IterationData> {
        val defaultIterationBuilder = DefaultIterationBuilder()
        val coercedIterationBuilder = CoercedIterationBuilder(defaultIterationBuilder)
        return when (val localTotalTime = timestamp.settings.totalTime) {
            is TimerDuration.Infinite -> {
                defaultIterationBuilder.build(
                    timestamp.settings,
                    now.minus(timestamp.start)
                )
            }

            is TimerDuration.Finite -> {
                coercedIterationBuilder.build(
                    timestamp.settings,
                    localTotalTime.instance
                )
            }
        }
    }

    private fun getCurrentIterationTypeTimeLeft(
        timestamp: TimerTimestamp.Running,
        now: Instant,
        currentIterationData: IterationData
    ): TimerDuration {
        return when {
            timestamp.settings.totalTime is TimerDuration.Infinite &&
                !timestamp.settings.intervalsSettings.isEnabled -> TimerDuration.Infinite

            else -> {
                when (currentIterationData) {
                    is IterationData.Default -> {
                        TimerDuration.Finite(
                            timestamp.start
                                .plus(currentIterationData.startOffset)
                                .plus(currentIterationData.duration)
                                .minus(now)
                        )
                    }

                    is IterationData.Infinite -> TimerDuration.Infinite
                    is IterationData.Pending -> {
                        TimerDuration.Finite(
                            timestamp.start
                                .plus(currentIterationData.startOffset)
                                .minus(now)
                        )
                    }
                }
            }
        }
    }

    @Suppress("LongParameterList")
    private fun getControlledTimerState(
        currentIterationData: IterationData,
        currentIterationTypeTimeLeft: TimerDuration,
        timestamp: TimerTimestamp.Running,
        currentIterationCount: Int,
        maxIterationCount: Int,
        now: Instant
    ): ControlledTimerState.InProgress {
        return when (currentIterationData.iterationType) {
            IterationType.Default.WORK -> ControlledTimerState.InProgress.Running.Work(
                timeLeft = currentIterationTypeTimeLeft,
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.Default.REST -> ControlledTimerState.InProgress.Running.Rest(
                timeLeft = currentIterationTypeTimeLeft,
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.Default.LONG_REST -> ControlledTimerState.InProgress.Running.LongRest(
                timeLeft = currentIterationTypeTimeLeft,
                isOnPause = timestamp.pause != null,
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                timePassed = now.minus(timestamp.start)
            )

            IterationType.Await.WAIT_AFTER_REST -> ControlledTimerState.InProgress.Await(
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                pausedAt = timestamp.start.plus(currentIterationData.startOffset),
                type = ControlledTimerState.InProgress.AwaitType.AFTER_REST
            )

            IterationType.Await.WAIT_AFTER_WORK -> ControlledTimerState.InProgress.Await(
                timerSettings = timestamp.settings,
                currentIteration = currentIterationCount,
                maxIterations = maxIterationCount,
                pausedAt = timestamp.start.plus(currentIterationData.startOffset),
                type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
            )
        }
    }

    override fun create(timestamp: TimerTimestamp): ControlledTimerState {
        if (timestamp !is TimerTimestamp.Running) {
            return ControlledTimerState.NotStarted
        }
        val now = timestamp.pause ?: timeProvider.now()

        val iterations = getIterations(
            timestamp = timestamp,
            now = now
        )

        val currentIterationData = getCurrentIteration(
            now = now,
            start = timestamp.start,
            confirmNextStepClick = timestamp.confirmNextStepClick,
            iterations = iterations
        ) ?: return ControlledTimerState.Finished(timerSettings = timestamp.settings)

        val maxIterationCount = iterations.count { it.iterationType == IterationType.Default.WORK }

        val currentIterationCount = iterations
            .subList(
                fromIndex = 0,
                toIndex = iterations
                    .indexOf(currentIterationData)
                    .plus(
                        other = when {
                            currentIterationData.iterationType == IterationType.Await.WAIT_AFTER_WORK -> 0
                            currentIterationData.iterationType != IterationType.Default.WORK -> -1
                            currentIterationData.iterationType == IterationType.Default.WORK -> 1
                            else -> 0
                        }
                    )
                    .coerceAtLeast(0)
            )
            .count { it.iterationType == IterationType.Default.WORK }

        val currentIterationTypeTimeLeft = getCurrentIterationTypeTimeLeft(
            timestamp = timestamp,
            now = now,
            currentIterationData = currentIterationData
        )

        return getControlledTimerState(
            currentIterationData = currentIterationData,
            currentIterationTypeTimeLeft = currentIterationTypeTimeLeft,
            timestamp = timestamp,
            currentIterationCount = currentIterationCount,
            maxIterationCount = maxIterationCount,
            now = now
        )
    }
}
