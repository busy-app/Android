package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppGraph::class)
interface WearSyncComponent {
    val timerApi: TimerApi
    val scope: CoroutineScope
    val wearConnectionApi: WearConnectionApi
}
