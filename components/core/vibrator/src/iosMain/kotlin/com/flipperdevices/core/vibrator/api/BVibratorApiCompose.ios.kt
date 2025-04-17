package com.flipperdevices.core.vibrator.api

import androidx.compose.runtime.Composable

@Composable
actual fun rememberVibratorApi(): BVibratorApi {
    return NoopVibratorApi
}