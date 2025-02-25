package com.flipperdevices.bsb.timer.background.util

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerTimestamp
import com.flipperdevices.bsb.timer.background.api.isOnPause
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.datetime.Clock

fun TimerApi.updateState(block: (TimerTimestamp?) -> TimerTimestamp?) {
    val newState = block.invoke(getTimestampState().value)
    setTimestampState(newState)
}

fun TimerApi.togglePause() {
    updateState { state ->
        if (state?.pause != null) {
            val diff = Clock.System.now() - state.pause
            state.copy(
                pause = null,
                start = state.start.plus(diff)
            )
        } else {
            state?.copy(pause = Clock.System.now())
        }.also { println("[LOGGER] #togglePause toggled pause: $it") }
    }
}

fun TimerApi.stop() {
    setTimestampState(null)
}

fun TimerApi.skip() {
    updateState { state ->
        state ?: return@updateState state
        val startedState = getState().value as? ControlledTimerState.Running ?: return@updateState state

        state.copy(start = state.start.minus(startedState.timeLeft))
    }
}

enum class IterationType {
    WORK, REST, LONG_REST
}

data class IterationData(
    val startOffset: Duration,
    val duration: Duration,
    val iterationType: IterationType
)

fun TimerSettings.buildIterationList(): List<IterationData> {
    if (!intervalsSettings.isEnabled) return listOf(IterationData(0.seconds, Duration.INFINITE, IterationType.WORK))
    return buildList {
        var timeLeft = totalTime
        var i = 0
        while (timeLeft > 0.seconds) {
            val type = when {
                i % 2 == 0 -> IterationType.WORK
                i % 3 == 2 -> IterationType.LONG_REST
                i % 2 == 1 -> IterationType.REST
                else -> error("#buildIterationList index was $i")
            }
            val iterationTypeDuration = when (type) {
                IterationType.WORK -> intervalsSettings.work
                IterationType.REST -> intervalsSettings.rest
                IterationType.LONG_REST -> intervalsSettings.longRest
            }
            add(
                IterationData(
                    startOffset = totalTime - timeLeft,
                    iterationType = type,
                    duration = iterationTypeDuration
                )
            )
            timeLeft -= iterationTypeDuration
            i += 1
        }
    }.also { println("[LOGGER] #buildIterationList $it") }
}

fun TimerApi.startWith(settings: TimerSettings) {
    setTimestampState(TimerTimestamp(settings = settings))
}


val TimerSettings.maxIterationCount: Int
    get() = buildIterationList()
        .count { data -> data.iterationType == IterationType.WORK }

fun TimerTimestamp?.toState(): ControlledTimerState {
    if (this == null) {
        println("[LOGGER] #toState State is null")
        return ControlledTimerState.NotStarted
    }
    val iterationList = settings.buildIterationList()

    // Filter only data which is not yet started
    val iterationsDataLeft = iterationList
        .filter { data -> Clock.System.now() < start.plus(data.startOffset).plus(data.duration) }
    val currentIterationData = iterationsDataLeft.firstOrNull()

    if (currentIterationData == null) return ControlledTimerState.Finished

    val iterationCountLeft = settings.maxIterationCount
        .minus(iterationsDataLeft.count { data -> data.iterationType == IterationType.WORK })

    val iterationTypeDuration = when (currentIterationData.iterationType) {
        IterationType.WORK -> settings.intervalsSettings.work
        IterationType.REST -> settings.intervalsSettings.rest
        IterationType.LONG_REST -> settings.intervalsSettings.longRest
    }

    val currentIterationTypeTimeLeft = when {
        pause != null -> {
            start
                .plus(currentIterationData.startOffset)
                .plus(iterationTypeDuration)
                .minus(pause)
        }

        else -> start
            .plus(currentIterationData.startOffset)
            .plus(iterationTypeDuration)
            .minus(Clock.System.now())
    }

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
    }.also {
        println("[LOGGER] #toState State is $it")
    }
}