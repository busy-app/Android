package com.flipperdevices.bsb.di

import com.flipperdevices.bsb.timer.syncservice.TimerSyncService
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppGraph::class)
@SingleIn(AppGraph::class)
abstract class DesktopAppComponent(
    override val observableSettings: ObservableSettings,
    override val scope: CoroutineScope
) : AppComponent {
    abstract val timerSyncService: TimerSyncService
}
