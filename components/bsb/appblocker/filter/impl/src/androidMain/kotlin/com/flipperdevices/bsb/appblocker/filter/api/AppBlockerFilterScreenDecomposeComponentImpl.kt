package com.flipperdevices.bsb.appblocker.filter.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerFilterScreenDecomposeComponent(
    @Assisted componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    private var isVisible by mutableStateOf(false)

    fun show() {
        isVisible = true
    }

    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            instance = if (isVisible) Unit else null,
            onDismiss = { isVisible = false }
        ) {
            BModalBottomSheetContent() {

            }
        }
    }
}