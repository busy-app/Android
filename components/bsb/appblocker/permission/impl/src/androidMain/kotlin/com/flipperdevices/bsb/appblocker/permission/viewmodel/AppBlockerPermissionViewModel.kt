package com.flipperdevices.bsb.appblocker.permission.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.activityholder.startActivity
import com.flipperdevices.core.ktx.android.highlightSettingsTo
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerPermissionViewModel(
    private val context: Context
) : DecomposeViewModel() {
    fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            .highlightSettingsTo(context.packageName)

        CurrentActivityHolder.startActivity(intent, context)
    }

    fun requestDrawOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        ).highlightSettingsTo(context.packageName)

        CurrentActivityHolder.startActivity(intent, context)
    }
}
