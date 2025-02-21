package com.flipperdevices.ui.timeline.util

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

internal fun DurationUnit.toLong(duration: Duration): Long {
    return when (this) {
        DurationUnit.NANOSECONDS -> duration.inWholeNanoseconds
        DurationUnit.MICROSECONDS -> duration.inWholeMicroseconds
        DurationUnit.MILLISECONDS -> duration.inWholeMilliseconds
        DurationUnit.SECONDS -> duration.inWholeSeconds
        DurationUnit.MINUTES -> duration.inWholeMinutes
        DurationUnit.HOURS -> duration.inWholeHours
        DurationUnit.DAYS -> duration.inWholeDays
        else -> error("wtf is $this")
    }
}

internal fun DurationUnit.toDuration(value: Int): Duration {
    return when (this) {
        DurationUnit.NANOSECONDS -> value.nanoseconds
        DurationUnit.MICROSECONDS -> value.microseconds
        DurationUnit.MILLISECONDS -> value.milliseconds
        DurationUnit.SECONDS -> value.seconds
        DurationUnit.MINUTES -> value.minutes
        DurationUnit.HOURS -> value.hours
        DurationUnit.DAYS -> value.days
        else -> error("wtf is $this")
    }
}
