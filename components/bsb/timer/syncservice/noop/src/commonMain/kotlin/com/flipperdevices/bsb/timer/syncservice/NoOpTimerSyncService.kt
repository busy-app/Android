package com.flipperdevices.bsb.timer.syncservice

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerSyncService::class)
interface NoOpTimerSyncService : TimerSyncService {
    override fun onCreate() = Unit
}
