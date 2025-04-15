package com.flipperdevices.core.trustedclock

import kotlinx.datetime.Instant

interface TrustedClock {
    fun initialize()

    fun now(): Instant
}
