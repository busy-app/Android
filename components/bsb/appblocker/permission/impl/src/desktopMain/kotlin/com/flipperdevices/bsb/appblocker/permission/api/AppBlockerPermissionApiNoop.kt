package com.flipperdevices.bsb.appblocker.permission.api

import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerPermissionApi::class)
class AppBlockerPermissionApiNoop() : AppBlockerPermissionApi {
    override fun isAllPermissionGranted() = flowOf(true)
}
