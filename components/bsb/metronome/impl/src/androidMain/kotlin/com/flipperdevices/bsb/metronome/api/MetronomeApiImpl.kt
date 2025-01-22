package com.flipperdevices.bsb.metronome.api

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, MetronomeApi::class)
class MetronomeApiImpl : MetronomeApi {
    override fun isMetronomeSupportActive() = false
    override fun enableSupport() = Result.failure<Unit>(NotImplementedError())
    override fun disableSupport() = Unit
    override fun play() = Unit
}
