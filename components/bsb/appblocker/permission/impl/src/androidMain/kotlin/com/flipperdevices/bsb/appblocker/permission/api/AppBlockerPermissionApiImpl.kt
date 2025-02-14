package com.flipperdevices.bsb.appblocker.permission.api

import android.content.Context
import com.flipperdevices.bsb.appblocker.permission.utils.PermissionChecker
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerPermissionApi::class)
class AppBlockerPermissionApiImpl(
    private val context: Context
) : AppBlockerPermissionApi {
    override fun isAllPermissionGranted(): StateFlow<Boolean> {
        return MutableStateFlow(
            PermissionChecker.hasUsageStatsPermission(context) &&
                    PermissionChecker.hasUsageStatsPermission(context)
        )
    }
}