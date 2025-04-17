package com.flipperdevices.core.vibrator.api

object NoopVibratorApi : BVibratorApi {
    override fun vibrateOnce(vibrateMode: VibrateMode) = Unit
}
