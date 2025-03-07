package com.flipperdevices.bsb.appblocker.screen.model

import android.content.Context
import android.content.pm.PackageManager
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo

data class InternalApplicationInfo(
    val packageName: String,
    val openCount: Int,
    val name: String
)

fun ApplicationInfo.toInternal(context: Context): InternalApplicationInfo {
    val applicationInfo = context.packageManager.getApplicationInfo(
        packageName,
        PackageManager.GET_META_DATA
    )
    val appName = context.packageManager.getApplicationLabel(applicationInfo).toString()

    return InternalApplicationInfo(
        packageName = packageName,
        openCount = openCount,
        name = appName
    )
}
