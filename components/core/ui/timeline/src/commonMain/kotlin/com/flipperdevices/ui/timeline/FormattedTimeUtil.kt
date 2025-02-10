package com.flipperdevices.ui.timeline

import kotlin.time.Duration

/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
@Suppress("MagicNumber")
internal fun Int.toFormattedTime(): String {
    return if (this < 10) "0$this" else "$this"
}

internal fun Duration.toFormattedTime(): String {
    return this.toComponents { days, hours, minutes, seconds, nanoseconds ->
        when {
            days > 0 -> "${days}d ${hours}h ${minutes.toFormattedTime()}m ${seconds.toFormattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.toFormattedTime()}m ${seconds.toFormattedTime()}s"
            minutes > 0 -> "${minutes}m ${seconds.toFormattedTime()}s"
            seconds == 0 -> "∞"
            else -> "${seconds}s"
        }
    }
}
