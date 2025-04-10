package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState.InProgress.Running
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface ControlledTimerState {
    @Serializable
    data object NotStarted : ControlledTimerState

    @Serializable
    data class Finished(
        val timerSettings: TimerSettings
    ) : ControlledTimerState

    @Serializable
    sealed interface InProgress : ControlledTimerState {
        val timerSettings: TimerSettings
        val currentIteration: Int
        val maxIterations: Int

        @Serializable
        sealed interface Running : InProgress {
            val timeLeft: TimerDuration
            val timePassed: Duration
            val isOnPause: Boolean

            @Serializable
            data class Work(
                override val timePassed: Duration,
                override val timeLeft: TimerDuration,
                override val isOnPause: Boolean,
                override val timerSettings: TimerSettings,
                override val currentIteration: Int,
                override val maxIterations: Int,
            ) : Running

            @Serializable
            data class Rest(
                override val timePassed: Duration,
                override val timeLeft: TimerDuration,
                override val isOnPause: Boolean,
                override val timerSettings: TimerSettings,
                override val currentIteration: Int,
                override val maxIterations: Int
            ) : Running

            @Serializable
            data class LongRest(
                override val timePassed: Duration,
                override val timeLeft: TimerDuration,
                override val isOnPause: Boolean,
                override val timerSettings: TimerSettings,
                override val currentIteration: Int,
                override val maxIterations: Int
            ) : Running
        }

        enum class AwaitType {
            AFTER_WORK, AFTER_REST
        }

        @Serializable
        data class Await(
            override val timerSettings: TimerSettings,
            override val currentIteration: Int,
            override val maxIterations: Int,
            val pausedAt: Instant,
            val type: AwaitType
        ) : InProgress
    }
}

private const val MIN_TWO_DIGIT_VALUE = 10

/**
 * Converts 0, 1, 2 -> 00, 01, 02
 */
private fun Long.fixedTwoDigitValue(): String {
    return if (this < MIN_TWO_DIGIT_VALUE) {
        "0$this"
    } else {
        "$this"
    }
}

private fun Int.fixedTwoDigitValue(): String {
    return this.toLong().fixedTwoDigitValue()
}

fun ControlledTimerState.InProgress.Running.toHumanReadableString(): String {
    val duration = when (val localTimeLeft = timeLeft) {
        is TimerDuration.Finite -> localTimeLeft.instance
        TimerDuration.Infinite -> timePassed
    }
    return duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        buildString {
            if (days > 0) append("${days.fixedTwoDigitValue()}:")
            if (days > 0 || hours > 0) append("${hours.fixedTwoDigitValue()}:")
            if (days > 0 || hours > 0 || minutes > 0) append("${minutes.fixedTwoDigitValue()}:")
            append(seconds.fixedTwoDigitValue())
        }
    }
}

val ControlledTimerState.InProgress.currentUiIteration: Int
    get() = currentIteration

val ControlledTimerState.InProgress.maxUiIterations: Int
    get() = maxIterations

val ControlledTimerState.InProgress.Running.maxTime: Duration
    get() = when (this) {
        is Running.LongRest -> timerSettings.intervalsSettings.longRest
        is Running.Rest -> timerSettings.intervalsSettings.rest
        is Running.Work -> timerSettings.intervalsSettings.work
    }

val ControlledTimerState.InProgress.Running.progress: Float
    get() {
        return when (val localTimeLeft = timeLeft) {
            is TimerDuration.Finite -> {
                when {
                    localTimeLeft.instance > maxTime -> 1f
                    maxTime.inWholeSeconds == 0L -> 0f
                    else -> localTimeLeft.instance.inWholeSeconds / maxTime.inWholeSeconds.toFloat()
                }
            }
            TimerDuration.Infinite -> 1f
        }
    }
