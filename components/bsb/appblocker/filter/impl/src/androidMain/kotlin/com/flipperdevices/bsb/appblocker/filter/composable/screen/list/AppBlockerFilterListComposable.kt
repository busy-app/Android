package com.flipperdevices.bsb.appblocker.filter.composable.screen.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation

@Composable
fun AppBlockerFilterListComposable(
    screenState: AppBlockerFilterScreenState.Loaded,
    switchApp: (
        uiAppInformation: UIAppInformation,
        checked: Boolean
    ) -> Unit,
    switchCategory: (
        appCategory: AppCategory,
        checked: Boolean
    ) -> Unit,
    categoryHideChange: (
        appCategory: AppCategory,
        checked: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier
            .fillMaxSize()
    ) {
        for (category in screenState.categories) {
            item(key = "category_${category.categoryEnum.id}") {
                CategoryFilterListItemComposable(
                    category = category,
                    onClick = { checked ->
                        categoryHideChange(category.categoryEnum, checked)
                    },
                    onSwitch = { checked ->
                        switchCategory(category.categoryEnum, checked)
                    },
                )
            }
            if (!category.isHidden) {
                items(
                    items = category.apps,
                    key = { "apps_${it.packageName}" },
                ) { app ->
                    AppFilterListItemComposable(
                        modifier = Modifier.padding(start = 24.dp),
                        appInfo = app,
                        onClick = { checked ->
                            switchApp(app, checked)
                        },
                    )
                }
            }
        }
    }
}
