package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_empty
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_loading
import com.flipperdevices.bsb.appblocker.filter.composable.screen.list.AppBlockerFilterListComposable
import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.list.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppInformation
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerFilterScreenComposable(
    screenState: AppBlockerFilterScreenState,
    query: String,
    onQuery: (String) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
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
    onSave: (currentState: AppBlockerFilterScreenState.Loaded) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (screenState) {
        AppBlockerFilterScreenState.Loading -> SimpleTextInformationComposable(
            text = Res.string.appblocker_filter_loading,
            modifier = modifier
        )

        is AppBlockerFilterScreenState.Loaded -> {
            Box(
                modifier,
                contentAlignment = Alignment.BottomCenter
            ) {
                Column {
                    AppBlockerFilterHeaderComposable(
                        screenState = screenState,
                        onSelectAll = onSelectAll,
                        onDeselectAll = onDeselectAll,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                    )
                    AppBlockerSearchBarComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                        query = query,
                        onQueryChange = onQuery
                    )
                    if (screenState.categories.isEmpty()) {
                        SimpleTextInformationComposable(
                            text = Res.string.appblocker_filter_empty,
                            modifier = Modifier
                        )
                    } else {
                        AppBlockerFilterListComposable(
                            screenState = screenState,
                            switchApp = switchApp,
                            switchCategory = switchCategory,
                            categoryHideChange = categoryHideChange,
                            modifier = Modifier.padding(vertical = 32.dp)
                        )
                    }
                }
                AppBlockerSaveButtonComposable(
                    onClick = {
                        onSave(screenState)
                    }
                )
            }
        }
    }
}

@Composable
private fun SimpleTextInformationComposable(
    text: StringResource,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(text),
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = LocalCorruptedPallet.current.neutral.tertiary,
        )
    }
}
