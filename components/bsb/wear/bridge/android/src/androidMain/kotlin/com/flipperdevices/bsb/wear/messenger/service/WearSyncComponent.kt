package com.flipperdevices.bsb.wear.messenger.service

import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.core.di.AppGraph
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import kotlinx.coroutines.CoroutineScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@OptIn(ExperimentalHorologistApi::class)
@ContributesTo(AppGraph::class)
interface WearSyncComponent {
    val timerApi: TimerApi
    val scope: CoroutineScope
    val krateApi: KrateApi
    val appBlockerFilterApi: AppBlockerFilterApi
    val wearConnectionApi: WearConnectionApi
}
