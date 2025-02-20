package com.flipperdevices.bsb.appblocker.permission.utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.flipperdevices.bsb.appblocker.permission.model.PermissionState
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class PermissionStateHolder(
    private val context: Context
) {
    private val state = MutableStateFlow(getStateSync())

    fun getState() = state.asStateFlow()

    fun invalidate() {
        state.value = getStateSync()
    }

    fun getStateSync(): PermissionState {
        return PermissionState(
            hasUsageStatsPermission = hasUsageStatsPermission(),
            hasDrawOverlayPermission = hasDrawOverlayPermission()
        )
    }

    private fun hasDrawOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOp(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                context.applicationInfo.uid,
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOp(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                context.applicationInfo.uid,
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
