package com.flipperdevices.core.vibrator.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberVibratorApi(): BVibratorApi {
    return remember { NoopVibratorApi() }
}