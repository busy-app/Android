package com.flipperdevices.bsb.timer.statefactory.iteration.datetime

import com.flipperdevices.core.di.AppGraph
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, TimeProvider::class)
class KotlinxDatetimeTimeProvider : TimeProvider {
    override fun now(): Instant {
        return Clock.System.now()
    }
}
