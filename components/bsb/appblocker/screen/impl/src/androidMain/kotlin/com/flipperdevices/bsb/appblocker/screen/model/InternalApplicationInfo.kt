package com.flipperdevices.bsb.appblocker.screen.model

import android.content.Context
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo

data class InternalApplicationInfo(
    val packageName: String,
    val openCount: Int,
    val name: String
)

fun ApplicationInfo.toInternal(context: Context): InternalApplicationInfo {
    val applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
    val appName = applicationInfo.loadLabel(context.packageManager).toString()

    return InternalApplicationInfo(
        packageName = packageName,
        openCount = openCount,
        name = appName
    )
}
