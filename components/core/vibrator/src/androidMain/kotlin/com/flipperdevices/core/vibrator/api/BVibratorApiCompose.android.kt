package com.flipperdevices.core.vibrator.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberVibratorApi(): BVibratorApi {
    val context = LocalContext.current
    return remember {
        AndroidVibratorApi(context)
    }
}