package com.flipperdevices.bsb.appblocker.filter.composable.screen.list

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flipperdevices.bsb.appblocker.filter.model.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppInformation

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
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier) {

    }
}

