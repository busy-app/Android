package com.flipperdevices.bsb.timer.background.api.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerTimestamp
import com.flipperdevices.bsb.timer.background.api.isOnPause
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.wtf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val logger = TaggedLogger("ControlledTimerStateFactory")

internal enum class IterationType {
    WORK, REST, LONG_REST
}

internal data class IterationData(
    val startOffset: Duration,
    val duration: Duration,
    val iterationType: IterationType
)

@Suppress("MagicNumber")
private fun getIterationTypeByIndex(i: Int): IterationType {
    return when {
        i % 2 == 0 -> IterationType.WORK
        i % 3 == 2 -> IterationType.LONG_REST
        i % 2 == 1 -> IterationType.REST
        else -> {
            logger.error { "#buildIterationList could not calculate next IterationType for index $i" }
            IterationType.WORK
        }
    }
}

@Suppress("LongMethod")
internal fun TimerSettings.buildIterationList(): List<IterationData> {
    if (!intervalsSettings.isEnabled) {
        return listOf(
            IterationData(
                startOffset = 0.seconds,
                duration = totalTime,
                iterationType = IterationType.WORK
            )
        )
    }
    val list = buildList {
        var timeLeft = totalTime
        var i = 0
        while (timeLeft > 0.seconds) {
            val type = getIterationTypeByIndex(i)
            val iterationTypeDuration = when (type) {
                IterationType.WORK -> intervalsSettings.work
                IterationType.REST -> intervalsSettings.rest
                IterationType.LONG_REST -> intervalsSettings.longRest
            }.coerceAtMost(timeLeft)
            when {
                timeLeft <= iterationTypeDuration && type == IterationType.WORK -> {
                    add(
                        IterationData(
                            startOffset = totalTime - timeLeft,
                            iterationType = IterationType.LONG_REST,
                            duration = iterationTypeDuration
                        )
                    )
                }

                timeLeft <= (iterationTypeDuration + intervalsSettings.work) && type == IterationType.REST -> {
                    add(
                        IterationData(
                            startOffset = totalTime - timeLeft,
                            iterationType = IterationType.LONG_REST,
                            duration = iterationTypeDuration
                                .plus(intervalsSettings.work)
                                .coerceAtMost(timeLeft)
                        )
                    )
                    timeLeft -= intervalsSettings.work
                }

                timeLeft <= (iterationTypeDuration + intervalsSettings.longRest) && type == IterationType.LONG_REST -> {
                    add(
                        IterationData(
                            startOffset = totalTime - timeLeft,
                            iterationType = IterationType.LONG_REST,
                            duration = iterationTypeDuration
                                .plus(intervalsSettings.longRest)
                                .coerceAtMost(timeLeft)
                        )
                    )
                    timeLeft -= intervalsSettings.longRest
                }

                else -> {
                    add(
                        IterationData(
                            startOffset = totalTime - timeLeft,
                            iterationType = type,
                            duration = iterationTypeDuration
                        )
                    )
                }
            }
            timeLeft -= iterationTypeDuration
            i += 1
        }
    }.toMutableList()
    logger.info { "#buildIterationList $list" }
    if (list.isEmpty()) {
        logger.wtf { "#buildIterationList was empty for $this" }
        return list
    }
    return list
}

private val TimerSettings.maxIterationCount: Int
    get() = buildIterationList()
        .count { data -> data.iterationType == IterationType.WORK }

internal fun calculateTimeLeft(
    start: Instant,
    pause: Instant?,
    duration: Duration,
    startOffset: Duration
): Duration {
    return when {
        pause != null -> {
            start
                .plus(startOffset)
                .plus(duration)
                .minus(pause)
        }

        else ->
            start
                .plus(startOffset)
                .plus(duration)
                .minus(Clock.System.now())
    }
}

internal fun TimerTimestamp?.toState(): ControlledTimerState {
    if (this == null) {
        return ControlledTimerState.NotStarted
    }
    val iterationList = settings.buildIterationList()

    // Filter only data which is not yet started
    val iterationsDataLeft = iterationList
        .filter { data -> Clock.System.now() <= start.plus(data.startOffset).plus(data.duration) }
    val currentIterationData = iterationsDataLeft.firstOrNull()

    if (currentIterationData == null) return ControlledTimerState.Finished

    val iterationCountLeft = settings.maxIterationCount
        .minus(iterationsDataLeft.count { data -> data.iterationType == IterationType.WORK })

    val currentIterationTypeTimeLeft = calculateTimeLeft(
        start = start,
        pause = pause,
        duration = currentIterationData.duration,
        startOffset = currentIterationData.startOffset
    )

    return when (currentIterationData.iterationType) {
        IterationType.WORK -> ControlledTimerState.Running.Work(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = isOnPause,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount
        )

        IterationType.REST -> ControlledTimerState.Running.Rest(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = isOnPause,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount
        )

        IterationType.LONG_REST -> ControlledTimerState.Running.LongRest(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = isOnPause,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount
        )
    }
}
