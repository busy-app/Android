package com.flipperdevices.core.data.timer

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val MIN_TWO_DIGIT_VALUE = 10

@Serializable
data class TimerState(
    val minute: Int,
    val second: Int
) {
    val duration: Duration
        get() = minute.minutes.plus(second.seconds)

    constructor(duration: Duration) : this(
        minute = duration.inWholeMinutes.toInt(),
        second = (duration.inWholeSeconds - duration.inWholeMinutes * 1.minutes.inWholeSeconds).toInt()
    )

    fun toHumanReadableString(): String {
        var text = ""
        if (minute < MIN_TWO_DIGIT_VALUE) {
            text += "0"
        }
        text += "$minute:"

        if (second < MIN_TWO_DIGIT_VALUE) {
            text += "0"
        }
        text += second
        return text
    }
}
