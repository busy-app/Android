package com.flipperdevices.bsb.appblocker.permission.model

data class PermissionState(
    val hasUsageStatsPermission: Boolean,
    val hasDrawOverlayPermission: Boolean
) {
    val isAllPermissionGranted: Boolean
        get() = hasDrawOverlayPermission && hasUsageStatsPermission
}
