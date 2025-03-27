package com.flipperdevices.bsb.timer.syncservice.di

import com.flipperdevices.bsb.cloud.mock.api.BSBMockApi
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.core.di.AppGraph
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppGraph::class)
interface SyncServiceDIComponent {
    val timerApi: TimerApi
    val krateApi: KrateApi
    val bsbMockApi: BSBMockApi
}
