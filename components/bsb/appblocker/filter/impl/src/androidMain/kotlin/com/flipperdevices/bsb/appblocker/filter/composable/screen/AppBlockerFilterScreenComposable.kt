package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_category_game
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_empty
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_loading
import com.flipperdevices.bsb.appblocker.filter.composable.screen.list.AppBlockerFilterListComposable
import com.flipperdevices.bsb.appblocker.filter.model.AppBlockerFilterScreenState
import com.flipperdevices.bsb.appblocker.filter.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.UIAppInformation
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerFilterScreenComposable(
    screenState: AppBlockerFilterScreenState,
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
    onSave: (currentState: AppBlockerFilterScreenState.Loaded) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (screenState) {
        AppBlockerFilterScreenState.Loading -> SimpleTextInformationComposable(
            text = Res.string.appblocker_filter_loading,
            modifier = modifier
        )

        is AppBlockerFilterScreenState.Loaded -> {
            if (screenState.categories.isEmpty()) {
                SimpleTextInformationComposable(
                    text = Res.string.appblocker_filter_loading,
                    modifier = modifier
                )
            } else {
                Column(modifier) {
                    AppBlockerFilterHeaderComposable(
                        screenState = screenState,
                        onSelectAll = onSelectAll,
                        onDeselectAll = onDeselectAll
                    )
                    AppBlockerFilterListComposable(
                        screenState = screenState,
                        switchApp = switchApp,
                        switchCategory = switchCategory,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleTextInformationComposable(
    modifier: Modifier = Modifier,
    text: StringResource
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(text),
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = LocalPallet.current.neutral.tertiary,
        )
    }
}