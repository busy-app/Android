package com.flipperdevices.bsb.appblocker.api

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.activityholder.startActivity
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.android.highlightSettingsTo
import com.flipperdevices.core.log.LogTagProvider
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerApi::class)
class AppBlockerApiImpl(
    private val context: Context,
    private val preferenceApi: PreferenceApi
) : AppBlockerApi, LogTagProvider {
    override val TAG = "AppBlockerApi"

    override fun isAppBlockerSupportActive(): Boolean {
        if (!hasAllPermission()) {
            return false
        }
        return preferenceApi.get(SettingsEnum.APP_BLOCKING, false)
    }

    override fun enableSupport(): Result<Unit> {
        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission()
            return Result.failure(RuntimeException("Usage stats permission not granted, waiting for user action"))
        }
        if (!Settings.canDrawOverlays(context)) {
            requestDrawOverlayPermission()
            return Result.failure(RuntimeException("Draw overlay permission not granted, waiting for user action"))
        }
        preferenceApi.set(SettingsEnum.APP_BLOCKING, true)
        return Result.success(Unit)
    }

    override fun disableSupport() {
        preferenceApi.set(SettingsEnum.APP_BLOCKING, false)
    }

    private fun hasAllPermission(): Boolean {
        if (hasUsageStatsPermission().not()) {
            return false
        }
        if (Settings.canDrawOverlays(context).not()) {
            return false
        }
        return true
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

    private fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            .highlightSettingsTo(context.packageName)

        CurrentActivityHolder.startActivity(intent, context)
    }

    private fun requestDrawOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        ).highlightSettingsTo(context.packageName)

        CurrentActivityHolder.startActivity(intent, context)
    }
}
