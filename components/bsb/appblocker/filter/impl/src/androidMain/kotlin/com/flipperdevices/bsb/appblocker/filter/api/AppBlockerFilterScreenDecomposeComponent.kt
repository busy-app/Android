package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.appblocker.filter.composable.screen.AppBlockerFilterScreenComposable
import com.flipperdevices.bsb.appblocker.filter.viewmodel.list.AppBlockerViewModelWithSearch
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerFilterScreenDecomposeComponent(
    @Assisted componentContext: ComponentContext,
    private val appBlockerStateBuilderFactory: () -> AppBlockerViewModelWithSearch
) : ElementDecomposeComponent(componentContext) {
    private var isVisible by mutableStateOf(false)
    private val viewModel by lazy {
        viewModelWithFactory(null) {
            appBlockerStateBuilderFactory()
        }
    }

    fun show() {
        isVisible = true
    }

    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            instance = if (isVisible) Unit else null,
            onDismiss = { isVisible = false },
            initialDetent = SheetDetent.FullyExpanded
        ) {
            BModalBottomSheetContent(
                horizontalPadding = 0.dp,
            ) {
                val state by viewModel.getState().collectAsState()
                val query by viewModel.getQuery().collectAsState()
                AppBlockerFilterScreenComposable(
                    modifier = Modifier.systemBarsPadding(),
                    screenState = state,
                    query = query,
                    onQuery = viewModel::onQuery,
                    onSelectAll = viewModel::selectAll,
                    onDeselectAll = viewModel::deselectAll,
                    onSave = {
                        viewModel.save(
                            currentState = it,
                            onHide = {
                                isVisible = false
                            }
                        )
                    },
                    switchApp = viewModel::switchApp,
                    switchCategory = viewModel::switchCategory,
                    categoryHideChange = viewModel::categoryHideChanged
                )
            }
        }
    }
}
