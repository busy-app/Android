package com.flipperdevices.bsb.appblocker.filter.viewmodel.list

import android.content.Context
import android.content.pm.ApplicationInfo
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
        if (apps.isEmpty()) {
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

        val appInfosByCategories = apps
            .filter { it.name != null }
            .groupBy { it.category }

        val categories = AppCategory.entries.map { category ->
            val isCategoryBlocked = checkedCategory.contains(category.id)
            val uiApps = appInfosByCategories.getOrDefault(category.id, emptyList())
                .mapNotNull {
                    it.toUIApp(
                        isBlocked = isCategoryBlocked ||
                            checkedAppsSet.contains(it.packageName)
                    )
                }
                .sortedBy { it.appName }
                .toPersistentList()

            UIAppCategory(
                categoryEnum = category,
                isBlocked = isCategoryBlocked,
                apps = uiApps,
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

            currentState.categories.forEach { category ->
                category.apps.filter { it.isBlocked }.forEach { app ->
                    database.appDao().insert(DBBlockedApp(app.packageName))
                }
            }
        }
    }

    private fun ApplicationInfo.toUIApp(
        isBlocked: Boolean
    ): UIAppInformation? {
        val label = context.packageManager.getApplicationLabel(this)
        if (label == this.name) {
            verbose {
                "Skip $this because label ($label) is name (${this.name})"
            }
            return null
        }
        return UIAppInformation(
            packageName = this.packageName,
            appName = label.toString(),
            category = AppCategory.fromCategoryId(category),
            isBlocked = isBlocked
        )
    }
}
