package com.flipperdevices.bsb.appblocker.filter.viewmodel

import android.content.Context
import com.flipperdevices.bsb.appblocker.filter.dao.AppFilterDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.repository.AppInformationDAO
import com.flipperdevices.bsb.appblocker.filter.model.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppInformation
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerViewModel(
    @Assisted private val context: Context,
    private val appInformationDAO: AppInformationDAO,
    private val database: AppFilterDatabase
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "AppBlockerViewModel"

    private val appBlockerFilterScreenState = MutableStateFlow<AppBlockerFilterScreenState>(
        AppBlockerFilterScreenState.Loading
    )

    fun getState() = appBlockerFilterScreenState.asStateFlow()

    fun selectAll() {
        appBlockerFilterScreenState.update { state ->
            when (state) {
                AppBlockerFilterScreenState.Loading -> state
                is AppBlockerFilterScreenState.Loaded -> {
                    AppBlockerFilterScreenState.Loaded(
                        state.categories.map {
                            it.block(isBlocked = true)
                        }.toPersistentList()
                    )
                }
            }
        }
    }

    fun deselectAll() {
        appBlockerFilterScreenState.update { state ->
            when (state) {
                AppBlockerFilterScreenState.Loading -> state
                is AppBlockerFilterScreenState.Loaded -> {
                    AppBlockerFilterScreenState.Loaded(
                        state.categories.map {
                            it.block(isBlocked = false)
                        }.toPersistentList()
                    )
                }
            }
        }
    }

    fun switchApp(
        uiAppInformation: UIAppInformation,
        checked: Boolean
    ) {
        appBlockerFilterScreenState.update { state ->
            when (state) {
                AppBlockerFilterScreenState.Loading -> state
                is AppBlockerFilterScreenState.Loaded -> state.updateCategory(uiAppInformation.category) {
                    it.blockApp(uiAppInformation.packageName, checked)
                }
            }
        }
    }

    fun switchCategory(
        appCategory: AppCategory,
        checked: Boolean
    ) {
        appBlockerFilterScreenState.update { state ->
            when (state) {
                AppBlockerFilterScreenState.Loading -> state
                is AppBlockerFilterScreenState.Loaded -> state.updateCategory(appCategory) {
                    it.copy(isBlocked = checked)
                }
            }
        }
    }

    /*
    private suspend fun load() {
        val packageManager = context.packageManager
        val apps = withContext(Dispatchers.Main) {
            packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
            ) as? List<ApplicationInfo>
        }
        val phoneApps = apps?.toPersistentList()
        if (phoneApps.isNullOrEmpty()) {
            appBlockerFilterScreenState.emit(
                AppBlockerFilterScreenState.Loaded(
                    apps = persistentListOf()
                )
            )
            return
        }

        val checkedAppsSet = appInformationDAO.getCheckedApps().map { it.appPackage }.toSet()
        val loadedApps = apps.map { applicationInfo ->
            UIAppInformation(
                packageName = applicationInfo.packageName,
                appName = applicationInfo.name,
                category = AppCategory.fromApplicationInfo(applicationInfo),
                isChecked = checkedAppsSet.contains(applicationInfo.packageName)
            )
        }.toPersistentList()

        appBlockerFilterScreenState.emit(
            AppBlockerFilterScreenState.Loaded(
                apps = loadedApps
            )
        )
    }

    fun save(currentState: AppBlockerFilterScreenState.Loaded) {
        viewModelScope.launch {
            saveInternal(currentState)
        }
    }

    private suspend fun saveInternal(currentState: AppBlockerFilterScreenState.Loaded) {
        database.withTransaction {
            appInformationDAO.dropTable()
            for (app in currentState.apps) {
                appInformationDAO.insert(DBBlockedApp(appPackage = app.packageName))
            }
        }
    }*/

}