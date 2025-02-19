package com.flipperdevices.bsb.appblocker.filter.viewmodel.list

import android.content.Context
import android.content.pm.PackageManager
import androidx.room.withTransaction
import com.flipperdevices.bsb.appblocker.filter.dao.AppFilterDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedApp
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.verbose
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerDaoHelper(
    private val context: Context,
    private val database: AppFilterDatabase
) : LogTagProvider {
    override val TAG = "AppBlockerDaoHelper"

    suspend fun load(): AppBlockerFilterScreenState.Loaded {
        val packageManager = context.packageManager
        val apps = withContext(Dispatchers.Main) {
            packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
            ).mapNotNull { it.applicationInfo }
        }
        val phoneApps = apps.toPersistentList()
        if (phoneApps.isEmpty()) {
            return AppBlockerFilterScreenState.Loaded(
                categories = persistentListOf()
            )
        }

        val checkedAppsSet = database
            .appDao()
            .getCheckedApps()
            .map { it.appPackage }
            .toSet()
        val checkedCategory = database
            .categoryDao()
            .getCheckedCategory()
            .map { it.categoryId }
            .toSet()

        val categories = apps
            .filter { it.name != null }
            .groupBy { it.category }
            .mapNotNull { (categoryId, applicationInfos) ->
                val category = AppCategory.fromCategoryId(categoryId)
                val isCategoryBlocked = checkedCategory.contains(categoryId)

                val apps = applicationInfos.map { applicationInfo ->
                    val label = packageManager.getApplicationLabel(applicationInfo)
                    if (label == applicationInfo.name) {
                        verbose {
                            "Skip $applicationInfo because label ($label) is name (${applicationInfo.name})"
                        }
                        return@mapNotNull null
                    }
                    UIAppInformation(
                        packageName = applicationInfo.packageName,
                        appName = label.toString(),
                        category = category,
                        isBlocked = isCategoryBlocked ||
                                checkedAppsSet.contains(applicationInfo.packageName)
                    )
                }.sortedBy { it.appName }
                UIAppCategory(
                    categoryEnum = category,
                    isBlocked = isCategoryBlocked,
                    apps = apps.toPersistentList(),
                    isHidden = true
                )
            }.sortedByDescending { it.categoryEnum.id }

        return AppBlockerFilterScreenState.Loaded(
            categories = categories.toPersistentList()
        )
    }

    suspend fun save(currentState: AppBlockerFilterScreenState.Loaded) {
        database.withTransaction {
            database.appDao().dropTable()
            database.categoryDao().dropTable()

            val blockedCategories = currentState.categories.filter { it.isBlocked }

            blockedCategories.forEach {
                database.categoryDao().insert(DBBlockedCategory(it.categoryEnum.id))
            }

            val nonBlockedCategories = currentState.categories.filter { !it.isBlocked }

            nonBlockedCategories.forEach { category ->
                category.apps.filter { it.isBlocked }.forEach { app ->
                    database.appDao().insert(DBBlockedApp(app.packageName))
                }
            }
        }
    }
}