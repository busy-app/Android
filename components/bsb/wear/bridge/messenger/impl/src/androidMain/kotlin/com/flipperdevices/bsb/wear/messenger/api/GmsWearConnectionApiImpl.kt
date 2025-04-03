package com.flipperdevices.bsb.wear.messenger.api

import com.flipperdevices.bsb.wear.messenger.util.nodesFlow
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.google.android.gms.wearable.CapabilityClient
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
@ContributesBinding(AppGraph::class, GmsWearConnectionApi::class)
class GmsWearConnectionApiImpl(
    capabilityClient: CapabilityClient,
    scope: CoroutineScope
) : GmsWearConnectionApi, LogTagProvider {
    override val TAG: String = "WearConnectionApi"
    override val statusFlow = capabilityClient.nodesFlow
        .map { nodes ->
            if (nodes.isEmpty()) {
                GmsWearConnectionApi.GmsStatus.Disconnected
            } else {
                GmsWearConnectionApi.GmsStatus.Connected(nodes)
            }
        }.stateIn(scope, SharingStarted.Companion.Eagerly, GmsWearConnectionApi.GmsStatus.Disconnected)
}
