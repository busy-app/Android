package com.flipperdevices.bsb.appblocker.permission.utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.provider.Settings

object PermissionChecker {
    fun hasDrawOverlayPermission(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun hasUsageStatsPermission(context: Context): Boolean {
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