package com.flipperdevices.core.trustedclock

import com.flipperdevices.core.di.AppGraph
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TrustedClock::class)
class KotlinxTrustedClock : TrustedClock {
    override fun initialize() = Unit

    override fun now(): Instant {
        return Clock.System.now()
    }
}
