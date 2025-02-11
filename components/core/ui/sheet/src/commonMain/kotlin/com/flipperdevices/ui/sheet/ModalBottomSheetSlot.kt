package com.flipperdevices.ui.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetScope
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

private val emptyContent: @Composable ModalBottomSheetScope.() -> Unit = {
    BModalBottomSheetContent { Box(Modifier) }
}

@Suppress("LambdaParameterInRestartableEffect")
@Composable
fun <C : Any> ModalBottomSheetSlot(
    instance: C?,
    onDismiss: () -> Unit,
    initialDetent: SheetDetent = SheetDetent.Companion.Hidden,
    content: @Composable ModalBottomSheetScope.(C) -> Unit
) {
    val childContent = remember { mutableStateOf(emptyContent) }

    val modalSheetState = rememberModalBottomSheetState(
        initialDetent = initialDetent
    )

    LaunchedEffect(modalSheetState) {
        snapshotFlow {
            modalSheetState.targetDetent == SheetDetent.Companion.Hidden &&
                modalSheetState.currentDetent == SheetDetent.Companion.Hidden &&
                modalSheetState.isIdle
        }
            .distinctUntilChanged()
            .drop(1)
            .collect { isHidden ->
                if (isHidden) {
                    onDismiss()
                }
            }
    }
    LaunchedEffect(instance) {
        if (instance == null && modalSheetState.currentDetent == SheetDetent.Companion.FullyExpanded) {
            modalSheetState.animateTo(SheetDetent.Companion.Hidden)
            childContent.value = emptyContent
            modalSheetState.jumpTo(SheetDetent.Companion.Hidden)
        } else if (instance != null && modalSheetState.currentDetent == SheetDetent.Companion.Hidden) {
            modalSheetState.animateTo(SheetDetent.Companion.FullyExpanded)
            childContent.value = { content(instance) }
        }
    }

    ModalBottomSheet(
        state = modalSheetState,
        content = {
            childContent.value.invoke(this)
        }
    )
}

@Composable
fun <C : Any, T : Any> ModalBottomSheetSlot(
    slot: ChildSlot<C, T>,
    onDismiss: () -> Unit,
    initialDetent: SheetDetent = SheetDetent.Companion.Hidden,
    content: @Composable ModalBottomSheetScope.(T) -> Unit
) {
    ModalBottomSheetSlot(
        initialDetent = initialDetent,
        instance = slot.child?.instance,
        onDismiss = onDismiss,
        content = content
    )
}

@Composable
fun <C : Any, T : Any> ModalBottomSheetSlot(
    slot: Value<ChildSlot<C, T>>,
    onDismiss: () -> Unit,
    initialDetent: SheetDetent = SheetDetent.Companion.Hidden,
    content: @Composable ModalBottomSheetScope.(T) -> Unit
) {
    ModalBottomSheetSlot(
        initialDetent = initialDetent,
        instance = slot.subscribeAsState().value.child?.instance,
        onDismiss = onDismiss,
        content = content
    )
}
