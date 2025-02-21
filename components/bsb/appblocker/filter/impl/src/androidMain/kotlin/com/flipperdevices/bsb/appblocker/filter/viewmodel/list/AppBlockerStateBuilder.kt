package com.flipperdevices.bsb.appblocker.filter.viewmodel.list

import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerStateBuilder(
    @Assisted private val scope: CoroutineScope,
    private val daoHelper: AppBlockerDaoHelper,
) : LogTagProvider {
    override val TAG = "AppBlockerViewModel"
    private val appBlockerFilterScreenState = MutableStateFlow<AppBlockerFilterScreenState>(
        AppBlockerFilterScreenState.Loading
    )

    init {
        scope.launch {
            appBlockerFilterScreenState.emit(daoHelper.load())
        }
    }

    fun getState() = appBlockerFilterScreenState.asStateFlow()

    fun save(
        currentState: AppBlockerFilterScreenState.Loaded,
        onHide: () -> Unit
    ) {
        scope.launch {
            daoHelper.save(currentState)
            withContext(Dispatchers.Main) {
                onHide()
            }
        }
    }

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
