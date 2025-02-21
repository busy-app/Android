package com.flipperdevices.bsb.appblocker.filter.viewmodel.list

import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerViewModelWithSearch(
    appBlockerStateBuilderFactory: (scope: CoroutineScope) -> AppBlockerStateBuilder
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "AppBlockerViewModelWithSearch"
    private val appBlockerStateBuilder = appBlockerStateBuilderFactory(viewModelScope)

    private val query = MutableStateFlow("")

    private val appBlockerFilterScreenState = MutableStateFlow<AppBlockerFilterScreenState>(
        AppBlockerFilterScreenState.Loading
    )

    init {
        combine(
            query,
            appBlockerStateBuilder.getState()
        ) { query, unfilteredState ->
            when (unfilteredState) {
                AppBlockerFilterScreenState.Loading -> unfilteredState
                is AppBlockerFilterScreenState.Loaded -> unfilteredState.copy(
                    categories = unfilteredState.categories.mapNotNull { category ->
                        SearchHelper.filterCategory(category, query)
                    }.toPersistentList()
                )
            }
        }.onEach {
            appBlockerFilterScreenState.emit(it)
        }.launchIn(viewModelScope)
    }

    fun getState() = appBlockerFilterScreenState.asStateFlow()
    fun getQuery() = query.asStateFlow()

    fun onQuery(text: String) {
        query.value = text
    }

    fun save(
        currentState: AppBlockerFilterScreenState.Loaded,
        onHide: () -> Unit
    ) = appBlockerStateBuilder.save(currentState, onHide)

    fun selectAll() = appBlockerStateBuilder.selectAll()

    fun deselectAll() = appBlockerStateBuilder.deselectAll()

    fun switchApp(
        uiAppInformation: UIAppInformation,
        checked: Boolean
    ) = appBlockerStateBuilder.switchApp(uiAppInformation, checked)

    fun switchCategory(
        appCategory: AppCategory,
        checked: Boolean
    ) = appBlockerStateBuilder.switchCategory(appCategory, checked)

    fun categoryHideChanged(
        appCategory: AppCategory,
        checked: Boolean
    ) = appBlockerStateBuilder.categoryHideChanged(appCategory, checked)
}
