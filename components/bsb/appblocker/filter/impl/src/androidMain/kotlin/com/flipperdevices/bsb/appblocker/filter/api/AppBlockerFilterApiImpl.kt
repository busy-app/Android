package com.flipperdevices.bsb.appblocker.filter.api

import android.content.Context
import android.content.pm.PackageManager
import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.appblocker.filter.dao.AppFilterDatabase
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerFilterApi::class)
class AppBlockerFilterApiImpl(
    private val context: Context,
    private val database: AppFilterDatabase,
    private val appBlockerApi: AppBlockerApi,
    private val permissionApi: AppBlockerPermissionApi
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

    override fun getBlockedAppCount(): Flow<BlockedAppCount> {
        return combine(
            appBlockerApi.isAppBlockerSupportActive(),
            permissionApi.isAllPermissionGranted(),
            database.categoryDao().getCheckedCategoryFlow()
                .map { categories ->
                    categories.map {
                        it.categoryId
                    }
                },
            database.appDao().getCheckedAppsCountFlow()
        ) { isBlockActive, isPermissionGranted, categoryIds, appsCount ->
            if (!isPermissionGranted) {
                BlockedAppCount.NoPermission
            } else if (!isBlockActive) {
                BlockedAppCount.TurnOff
            } else if (AppCategory.isAllCategoryContains(categoryIds)) {
                BlockedAppCount.All
            } else {
                BlockedAppCount.Count(categoryIds.count() + appsCount)
            }
        }
    }
}
