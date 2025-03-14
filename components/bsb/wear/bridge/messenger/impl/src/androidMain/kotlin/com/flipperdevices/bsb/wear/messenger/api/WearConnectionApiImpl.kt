package com.flipperdevices.bsb.wear.messenger.api

import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.collections.map

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, WearConnectionApi::class)
class WearConnectionApiImpl(
    gmsWearConnectionApi: GmsWearConnectionApi,
    scope: CoroutineScope
) : WearConnectionApi, LogTagProvider {
    override val TAG: String = "WearConnectionApi"
    override val statusFlow = gmsWearConnectionApi.statusFlow.map {
        when (it) {
            is GmsWearConnectionApi.GmsStatus.Connected -> WearConnectionApi.Status.Connected
            GmsWearConnectionApi.GmsStatus.Disconnected -> WearConnectionApi.Status.Disconnected
        }
    }.stateIn(scope, SharingStarted.Companion.Eagerly, WearConnectionApi.Status.Disconnected)
}
