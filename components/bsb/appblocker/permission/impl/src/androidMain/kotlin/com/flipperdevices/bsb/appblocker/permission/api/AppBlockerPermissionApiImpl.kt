package com.flipperdevices.bsb.appblocker.permission.api

import com.flipperdevices.bsb.appblocker.permission.utils.PermissionStateHolder
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerPermissionApi::class)
class AppBlockerPermissionApiImpl(
    private val stateHolder: PermissionStateHolder
) : AppBlockerPermissionApi {
    override fun isAllPermissionGranted(): Flow<Boolean> {
        return stateHolder.getState().map {
            it.isAllPermissionGranted
        }
    }
}
