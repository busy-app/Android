package com.flipperdevices.core.vibrator.api

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding


@Inject
@ContributesBinding(AppGraph::class, BVibratorApi::class)
class NoopVibratorApi : BVibratorApi {
    override fun vibrateOnce(vibrateMode: VibrateMode) = Unit
}
