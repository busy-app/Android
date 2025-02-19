package com.flipperdevices.bsb.appblocker.filter.viewmodel.card

import com.flipperdevices.bsb.appblocker.filter.dao.AppFilterDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.repository.AppInformationDao
import com.flipperdevices.bsb.appblocker.filter.dao.repository.CategoryInformationDao
import com.flipperdevices.bsb.appblocker.filter.model.card.AppBlockerCardListState
import com.flipperdevices.bsb.appblocker.filter.model.card.AppIcon
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerCardListViewModel(
    database: AppFilterDatabase
) : DecomposeViewModel() {
    private val listState = MutableStateFlow<AppBlockerCardListState>(AppBlockerCardListState.Empty)

    init {
        combine(
            database.categoryDao().getCheckedCategoryFlow()
                .map { categories ->
                    categories.map {
                        AppIcon.Category(AppCategory.fromCategoryId(it.categoryId).icon)
                    }
                },
            database.appDao().getCheckedAppsFlow()
                .map { apps ->
                    apps.map { AppIcon.App(it.appPackage) }
                }
        ) { categories, apps ->
            categories + apps
        }.onEach { icons ->
            val state = if (icons.isEmpty()) {
                AppBlockerCardListState.Empty
            } else {
                AppBlockerCardListState.Loaded(icons.toPersistentList())
            }
            listState.emit(state)
        }.launchIn(viewModelScope)
    }

    fun getState() = listState.asStateFlow()
}