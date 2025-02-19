package com.flipperdevices.bsb.appblocker.filter.api

import android.content.Context
import android.content.pm.PackageManager
import com.flipperdevices.bsb.appblocker.filter.dao.AppFilterDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerFilterApi::class)
class AppBlockerFilterApiImpl(
    private val context: Context,
    private val database: AppFilterDatabase
) : AppBlockerFilterApi {
    override suspend fun isBlocked(packageName: String): Boolean = runCatching {
        val blockedApps = database.appDao().find(packageName)
        if (blockedApps.isNotEmpty()) {
            return@runCatching true
        }
        val applicationInfo = context
            .packageManager
            .getApplicationInfo(packageName, PackageManager.GET_META_DATA)

        val blockedCategories = database.categoryDao().find(applicationInfo.category)
        if (blockedCategories.isNotEmpty()) {
            return@runCatching true
        }

        return@runCatching false
    }.getOrDefault(false)
}