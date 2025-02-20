package com.flipperdevices.bsb.appblocker.listener

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import me.tatarka.inject.annotations.Inject

@Inject
class PackageFilter(
    context: Context,
    private val filterApi: AppBlockerFilterApi
) {
    private val myPackageName by lazy { context.packageName }
    private val launcherPackageName by lazy {
        context.launcherPackageName()
    }

    suspend fun isForbidden(packageName: String): Boolean {
        if (packageName == myPackageName) {
            return false // Busy application
        }
        if (packageName == launcherPackageName) {
            return false // Launcher application
        }
        return filterApi.isBlocked(packageName)
    }
}

private fun Context.launcherPackageName(): String? {
    val intent = Intent("android.intent.action.MAIN")
    intent.addCategory("android.intent.category.HOME")
    return packageManager
        .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        ?.activityInfo
        ?.packageName
}
