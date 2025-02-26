package com.flipperdevices.bsb.timer.background.notification

import android.Manifest
import android.os.Build
import com.flipperdevices.core.activityholder.CurrentActivityHolder

object NotificationPermissionHelper {
    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val activity = CurrentActivityHolder.getCurrentActivity()
            activity?.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
    }
}
