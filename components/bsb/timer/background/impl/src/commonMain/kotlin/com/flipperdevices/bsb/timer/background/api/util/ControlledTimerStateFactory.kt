package com.flipperdevices.bsb.timer.background.api.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.wtf
import kotlinx.datetime.Clock
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

            val isNoTimeForWorkLeft = timeLeft <= iterationTypeDuration
                    && type == IterationType.WORK

            val isNoTimeForShortRestLeft = timeLeft <= (iterationTypeDuration + intervalsSettings.work)
                    && type == IterationType.REST

            val isLongRestNeedMoreTimeThanTimeLeft = timeLeft <= (iterationTypeDuration + intervalsSettings.longRest)
                    && type == IterationType.LONG_REST

            IterationData(
                startOffset = totalTime - timeLeft,
                iterationType = when {
                    isNoTimeForWorkLeft
                        .or(isNoTimeForShortRestLeft)
                        .or(isLongRestNeedMoreTimeThanTimeLeft) -> IterationType.LONG_REST

                    else -> type
                },
                duration = when {
                    isNoTimeForShortRestLeft -> iterationTypeDuration
                        .plus(intervalsSettings.work)
                        .coerceAtMost(timeLeft)
                        .also { timeLeft -= intervalsSettings.work }

                    isLongRestNeedMoreTimeThanTimeLeft -> iterationTypeDuration
                        .plus(intervalsSettings.longRest)
                        .coerceAtMost(timeLeft)
                        .also { timeLeft -= intervalsSettings.work }

                    isNoTimeForWorkLeft -> iterationTypeDuration
                    else -> iterationTypeDuration
                }
            ).run(::add)

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

internal fun TimerTimestamp?.toState(): ControlledTimerState {
    if (this == null) {
        return ControlledTimerState.NotStarted
    }
    val iterationList = settings.buildIterationList()
    val now = pause ?: Clock.System.now()

    // Filter only data which is not yet started
    val iterationsDataLeft = iterationList
        .filter { data -> now <= start.plus(data.startOffset).plus(data.duration) }
    val currentIterationData = iterationsDataLeft.firstOrNull()

    if (currentIterationData == null) return ControlledTimerState.Finished

    val iterationCountLeft = settings.maxIterationCount
        .minus(iterationsDataLeft.count { data -> data.iterationType == IterationType.WORK })

    val currentIterationTypeTimeLeft = start
        .plus(currentIterationData.startOffset)
        .plus(currentIterationData.duration)
        .minus(now)

    return when (currentIterationData.iterationType) {
        IterationType.WORK -> ControlledTimerState.Running.Work(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
        )

        IterationType.REST -> ControlledTimerState.Running.Rest(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
        )

        IterationType.LONG_REST -> ControlledTimerState.Running.LongRest(
            timeLeft = currentIterationTypeTimeLeft,
            isOnPause = pause != null,
            timerSettings = settings,
            currentIteration = iterationCountLeft,
            maxIterations = settings.maxIterationCount,
        )
    }
}
