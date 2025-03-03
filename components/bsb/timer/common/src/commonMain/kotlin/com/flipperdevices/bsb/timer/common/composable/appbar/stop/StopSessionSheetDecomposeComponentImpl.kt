package com.flipperdevices.bsb.timer.common.composable.appbar.stop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.composables.core.Scrim
import com.composables.core.SheetDetent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class StopSessionSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onConfirm: () -> Unit
) : ComponentContext by componentContext {
    private val slot = SlotNavigation<Unit>()
    private val childSlot = childSlot(
        source = slot,
        serializer = Unit.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    fun show() {
        slot.activate(Unit)
    }

    @Composable
    fun Render(hazeState: HazeState) {
        val coroutineScope = rememberCoroutineScope()
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.Hidden,
            onDismiss = { slot.dismiss() },
            content = {
                Scrim()
                InvisibleSheet {
                    StopSessionComposableContent(
                        onConfirm = {
                            slot.dismiss()
                            coroutineScope.launch {
                                // Wait a bit so bottom sheet animation will complete
                                delay(timeMillis = 200L)
                                onConfirm.invoke()
                            }
                        },
                        onDismiss = { slot.dismiss() },
                        hazeState = hazeState
                    )
                }
            }
        )
    }
}
