package com.flipperdevices.bsb.timer.active.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.timer.active.composable.StopSessionComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class StopSessionSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onConfirm: () -> Unit
) : StopSessionSheetDecomposeComponent(componentContext) {

    private val slot = SlotNavigation<Unit>()
    private val childSlot = childSlot(
        source = slot,
        serializer = Unit.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    override fun show() {
        slot.activate(Unit)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = {
                BModalBottomSheetContent {
                    StopSessionComposableContent(
                        onConfirm = {
                            slot.dismiss()
                            coroutineScope.launch {
                                // Wait a bit so bottom sheet animation will complete
                                delay(timeMillis = 200L)
                                onConfirm.invoke()
                            }
                        },
                        onDismiss = { slot.dismiss() }
                    )
                }
            }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, StopSessionSheetDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onConfirm: () -> Unit
        ) -> StopSessionSheetDecomposeComponentImpl
    ) : StopSessionSheetDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onConfirm: () -> Unit
        ) = factory(componentContext, onConfirm)
    }
}
