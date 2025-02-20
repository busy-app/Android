package com.flipperdevices.bsb.appblocker.filter.api

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerFilterApi::class)
class AppBlockerFilterApiNoop : AppBlockerFilterApi {
    override suspend fun isBlocked(packageName: String) = false
    override fun getBlockedAppCount() = flowOf(BlockedAppCount.TurnOff)
}
