package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
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
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class IntervalsSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : ScreenDecomposeComponent(componentContext) {

    private val slot = SlotNavigation<PickerConfiguration>()
    private val childSlot = childSlot(
        source = slot,
        serializer = PickerConfiguration.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    @Serializable
    private sealed interface PickerConfiguration {
        @Serializable
        data object Rest : PickerConfiguration

        @Serializable
        data object LongRest : PickerConfiguration

        @Serializable
        data object Cycles : PickerConfiguration
    }

    fun showRest() {
        slot.activate(PickerConfiguration.Rest)
    }

    fun showLongRest() {
        slot.activate(PickerConfiguration.LongRest)
    }

    fun showCycles() {
        slot.activate(PickerConfiguration.Cycles)
    }

    fun dismiss() {
        slot.dismiss()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = {
                when (it) {
                    PickerConfiguration.Cycles -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Cycles",
                                desc = "Pick how many focus and rest cycles you want to complete during your session",
                                onSaveClick = { slot.dismiss() },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..10 step 1,
                                    initialValue = 4
                                )
                            )
                        }
                    }

                    PickerConfiguration.LongRest -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Long rest",
                                desc = "Pick how long you want to relax after completing several cycles",
                                postfix = "min",
                                onSaveClick = { slot.dismiss() },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..60 step 5,
                                    initialValue = 15
                                )
                            )
                        }
                    }

                    PickerConfiguration.Rest -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Rest",
                                desc = "Pick how long you want to rest before starting the next focus session",
                                postfix = "min",
                                onSaveClick = { slot.dismiss() },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..60 step 5,
                                    initialValue = 5
                                )
                            )
                        }
                    }
                }
            }
        )
    }
}
