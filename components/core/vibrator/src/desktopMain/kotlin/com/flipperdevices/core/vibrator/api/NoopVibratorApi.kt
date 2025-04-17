package com.flipperdevices.core.vibrator.api

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration

object NoopVibratorApi : BVibratorApi {
    override fun vibrateOnce(vibrateMode: VibrateMode) = Unit
}
