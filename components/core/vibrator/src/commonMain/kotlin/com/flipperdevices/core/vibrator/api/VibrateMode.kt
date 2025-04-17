package com.flipperdevices.core.vibrator.api

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

enum class VibrateMode(val duration: Duration) {
    TICK(500.milliseconds),
    THUD(1.seconds)
}