package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.filter.composable.screen.AppBlockerFilterScreenComposable
import com.flipperdevices.bsb.appblocker.filter.viewmodel.AppBlockerViewModel
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerFilterScreenDecomposeComponent(
    @Assisted componentContext: ComponentContext,
    private val appBlockerViewModelFactory: () -> AppBlockerViewModel
) : ElementDecomposeComponent(componentContext) {
    private var isVisible by mutableStateOf(false)
    private val viewModel = viewModelWithFactory(null) {
        appBlockerViewModelFactory()
    }

    fun show() {
        isVisible = true
    }

    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            instance = if (isVisible) Unit else null,
            onDismiss = { isVisible = false },
        ) {
            BModalBottomSheetContent(
                horizontalPadding = 0.dp
            ) {
                val state by viewModel.getState().collectAsState()
                AppBlockerFilterScreenComposable(
                    screenState = state,
                    onSelectAll = viewModel::selectAll,
                    onDeselectAll = viewModel::deselectAll,
                    onSave = {},
                    switchApp = viewModel::switchApp,
                    switchCategory = viewModel::switchCategory,
                    categoryHideChange = viewModel::categoryHideChanged
                )
            }
        }
    }
}