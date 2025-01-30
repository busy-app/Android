package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.timer.setup.composable.PickerContent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.picker.rememberTimerState
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RestSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : ScreenDecomposeComponent(componentContext) {

    private val slot = SlotNavigation<Unit>()
    private val childSlot = childSlot(
        source = slot,
        serializer = Unit.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    fun openSheet() {
        slot.activate(Unit)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val minutesState = rememberTimerState(
            count = 2,
            initialNumber = 1
        )

        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = {
                BModalBottomSheetContent {
                    PickerContent(
                        title = "Rest",
                        desc = "Pick how long you want to rest before starting the next focus session",
                        onSaveClick = { slot.dismiss() },
                        minutesState = minutesState
                    )
                }
            }
        )
    }
}
