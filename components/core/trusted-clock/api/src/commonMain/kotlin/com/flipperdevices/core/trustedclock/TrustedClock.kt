package com.flipperdevices.core.trustedclock

import kotlinx.datetime.Instant

fun interface TrustedClock {
    fun now(): Instant
}
