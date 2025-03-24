package com.flipperdevices.core.vibrator.api

import kotlin.time.Duration

interface BVibratorApi {
    fun vibrateOnce(duration: Duration)
}
