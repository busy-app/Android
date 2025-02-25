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

fun Duration.toFormattedTime(slim: Boolean = true): String {
    if (inWholeSeconds == 0L) {
        return "∞"
    }
    return this.toComponents { days, hours, minutes, seconds, _ ->
        val builder = StringBuilder()
        if (days > 0) {
            builder.append(days)
            if (slim.not()) {
                builder.append(' ')
            }
            builder.append("d ")
        }
        if (hours > 0) {
            val hoursText = if (builder.isBlank()) {
                hours.toString()
            } else {
                hours.toFormattedTime()
            }
            builder.append(hoursText)
            if (slim.not()) {
                builder.append(' ')
            }
            builder.append("h ")
        }
        if (minutes > 0) {
            val minutesText = if (builder.isBlank()) {
                minutes.toString()
            } else {
                minutes.toFormattedTime()
            }
            builder.append(minutesText)
            if (slim.not()) {
                builder.append(' ')
            }
            builder.append("m ")
        }
        if (seconds > 0) {
            val secondsText = if (builder.isBlank()) {
                seconds.toString()
            } else {
                seconds.toFormattedTime()
            }
            builder.append(secondsText)
            if (slim.not()) {
                builder.append(' ')
            }
            builder.append("s")
        }

        builder.toString().trim()
    }
}
