package com.flipperdevices.bsb.appblocker.filter.viewmodel

import com.flipperdevices.bsb.appblocker.filter.model.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppInformation
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerViewModel(
    private val daoHelper: AppBlockerDaoHelper
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "AppBlockerViewModel"

    private val appBlockerFilterScreenState = MutableStateFlow<AppBlockerFilterScreenState>(
        AppBlockerFilterScreenState.Loading
    )

    init {
        viewModelScope.launch {
            appBlockerFilterScreenState.emit(daoHelper.load())
        }
    }

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
                    it.block(isBlocked = checked)
                }
            }
        }
    }


    fun categoryHideChanged(
        appCategory: AppCategory,
        checked: Boolean
    ) {
        appBlockerFilterScreenState.update { state ->
            when (state) {
                AppBlockerFilterScreenState.Loading -> state
                is AppBlockerFilterScreenState.Loaded -> state.updateCategory(appCategory) {
                    it.copy(isHidden = checked)
                }
            }
        }
    }

}