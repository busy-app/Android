package com.flipperdevices.bsb.wear.messenger.api

import com.flipperdevices.bsb.wear.messenger.util.nodesFlow
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.wtf
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
@ContributesBinding(AppGraph::class, WearConnectionApi::class)
class WearConnectionApiImpl(
    capabilityClient: CapabilityClient,
    scope: CoroutineScope
) : WearConnectionApi, LogTagProvider {
    override val TAG: String = "WearConnectionApi"
    override val statusFlow = capabilityClient.nodesFlow
        .map { nodes ->
            when {
                nodes.isEmpty() -> WearConnectionApi.Status.Disconnected
                nodes.size > 1 -> {
                    wtf { "#statusFlow more than 1 nodes received ${nodes.map { node -> node.displayName }}" }
                    WearConnectionApi.Status.Connected(nodes.first())
                }

                else -> WearConnectionApi.Status.Connected(nodes.first())
            }
        }.stateIn(scope, SharingStarted.Companion.Eagerly, WearConnectionApi.Status.Disconnected)
}
