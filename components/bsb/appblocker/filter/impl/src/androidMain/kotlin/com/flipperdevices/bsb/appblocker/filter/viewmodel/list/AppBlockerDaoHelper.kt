package com.flipperdevices.bsb.appblocker.filter.viewmodel.list

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.flipperdevices.bsb.appblocker.filter.api.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.bsb.appblocker.filter.model.list.fromCategoryId
import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.verbose
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerDaoHelper(
    private val cardAppBlockerApi: CardAppBlockerApi,
    private val context: Context,
) : LogTagProvider {
    override val TAG = "AppBlockerDaoHelper"

    suspend fun load(cardId: TimerSettingsId): AppBlockerFilterScreenState.Loaded {
        val packageManager = context.packageManager
        val apps = withContext(Dispatchers.Main) {
            packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
            ).mapNotNull { it.applicationInfo }
        }.filter { it.packageName != context.packageName }
        if (apps.isEmpty()) {
            return AppBlockerFilterScreenState.Loaded(
                categories = persistentListOf()
            )
        }

        val currentState = cardAppBlockerApi.getBlockedAppDetailedState(cardId)
            .first()

        val checkedAppsSet = when (currentState) {
            BlockedAppDetailedState.All,
            BlockedAppDetailedState.TurnOff -> emptySet()

            is BlockedAppDetailedState.TurnOnWhitelist ->
                currentState
                    .entities
                    .filterIsInstance<BlockedAppEntity.App>()
                    .map { it.packageId }
                    .toSet()
        }

        val checkedCategory = when (currentState) {
            BlockedAppDetailedState.All,
            BlockedAppDetailedState.TurnOff -> AppCategory.entries.toSet()

            is BlockedAppDetailedState.TurnOnWhitelist ->
                currentState
                    .entities
                    .filterIsInstance<BlockedAppEntity.Category>()
                    .map { AppCategory.fromCategoryId(it.categoryId) }
                    .toSet()
        }

        val appInfosByCategories = apps
            .filter { it.name != null }
            .groupBy { it.category }

        val categories = AppCategory.entries.map { category ->
            val isCategoryBlocked = checkedCategory.contains(category)
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

    suspend fun save(
        cardId: TimerSettingsId,
        currentState: AppBlockerFilterScreenState.Loaded
    ) {
        val isBlockedAll = currentState.categories.all { it.isBlocked }
        val blockedState = if (isBlockedAll) {
            BlockedAppDetailedState.All
        } else {
            val apps = currentState.categories.map { it.apps }.flatten().map {
                BlockedAppEntity.App(it.packageName)
            }
            val category = currentState.categories.map {
                BlockedAppEntity.Category(it.categoryEnum.id)
            }
            BlockedAppDetailedState.TurnOnWhitelist(apps + category)
        }

        cardAppBlockerApi.updateBlockedApp(cardId, blockedState)
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
