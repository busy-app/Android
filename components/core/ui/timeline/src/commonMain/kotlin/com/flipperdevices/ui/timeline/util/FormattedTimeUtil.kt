package com.flipperdevices.ui.timeline.util

import kotlin.time.Duration

private const val SINGLE_CHAR_NUMBER_LIMIT = 10

/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
fun Int.toFormattedTime(): String {
    return if (this < SINGLE_CHAR_NUMBER_LIMIT) "0$this" else "$this"
}

fun Duration.toFormattedTime(): String {
    return this.toComponents { days, hours, minutes, seconds, nanoseconds ->
        when {
            days > 0 -> "${days}d ${hours}h ${minutes.toFormattedTime()}m ${seconds.toFormattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.toFormattedTime()}m"
            minutes > 0 -> "${minutes}m ${seconds.toFormattedTime()}s"
            seconds == 0 -> "∞"
            else -> "${seconds}s"
        }
    }
}
