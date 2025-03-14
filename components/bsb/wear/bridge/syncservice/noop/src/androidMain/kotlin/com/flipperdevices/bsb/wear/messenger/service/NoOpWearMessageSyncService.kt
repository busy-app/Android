package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearMessageSyncService::class)
class NoOpWearMessageSyncService : WearMessageSyncService {
    override val TAG = "NoOpWearMessageSyncService"
}
