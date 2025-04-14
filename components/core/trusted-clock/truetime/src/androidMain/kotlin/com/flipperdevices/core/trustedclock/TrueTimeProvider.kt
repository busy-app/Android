package com.flipperdevices.core.trustedclock

import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.warn
import com.instacart.truetime.time.TrueTimeImpl
import com.instacart.truetime.time.TrueTimeParameters
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TrustedClock::class)
class TrueTimeProvider : TrustedClock, LogTagProvider {
    override val TAG: String = "TrueTimeProvider"

    private var trueTime = TrueTimeImpl(
        params = TrueTimeParameters.Builder()
            .ntpHostPool(arrayListOf("time.cloudflare.com"))
            .buildParams(),
        listener = TrueTimeLoggerListener()
    )

    override fun initialize() {
        info { "Create task on trusted time api initialize" }
        trueTime.sync()
    }

    override fun now(): Instant {
        if (!trueTime.hasTheTime()) {
            warn { "Requested the exact time, but the true time ntp has not time" }
            return Clock.System.now()
        }

        val instant = runCatching { trueTime.nowTrueOnly() }.getOrNull()
        if (instant == null) {
            warn { "TrueTime is initialized, but current instant is incorrect" }
            return Clock.System.now()
        }

        return instant.toInstant().toKotlinInstant()
    }
}
