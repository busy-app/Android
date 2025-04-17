package com.flipperdevices.ui.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetScope
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen

private val emptyContent: @Composable ModalBottomSheetScope.() -> Unit = {
    BModalBottomSheetContent { Box(Modifier) }
}

private sealed interface LogicEvent<out C> {
    data object Close : LogicEvent<Nothing>
    data class Open<C>(val instance: C) : LogicEvent<C>
    data class CloseAndOpen<C>(val instance: C) : LogicEvent<C>
    data object Unhandled : LogicEvent<Nothing>
}

private suspend fun <T> withRetry(block: suspend () -> T): T {
    return flow { emit(block.invoke()) }
        .retryWhen { _, _ -> true }
        .first()
}

private fun <C : Any> getLogicEventFlow(
    instance: C?,
    modalSheetState: ModalBottomSheetState
): Flow<LogicEvent<C>> {
    return snapshotFlow {
        when {
            instance == null && modalSheetState.currentDetent == SheetDetent.Companion.FullyExpanded -> {
                LogicEvent.Close
            }

            instance != null &&
                modalSheetState.currentDetent == SheetDetent.Companion.Hidden &&
                modalSheetState.targetDetent != SheetDetent.Companion.FullyExpanded -> {
                LogicEvent.CloseAndOpen(instance)
            }

            instance != null && modalSheetState.currentDetent == SheetDetent.Companion.Hidden -> {
                LogicEvent.Open(instance)
            }

            else -> LogicEvent.Unhandled
        }
    }
}

private suspend fun <C : Any> handleLogicEvent(
    logicEvent: LogicEvent<C>,
    modalSheetState: ModalBottomSheetState,
    targetDetent: SheetDetent,
    childContent: MutableState<@Composable ModalBottomSheetScope.() -> Unit>,
    content: @Composable ModalBottomSheetScope.(C) -> Unit
) {
    when (logicEvent) {
        LogicEvent.Close -> {
            withRetry { modalSheetState.animateTo(SheetDetent.Companion.Hidden) }
            childContent.value = emptyContent
            modalSheetState.jumpTo(SheetDetent.Companion.Hidden)
        }

        is LogicEvent.CloseAndOpen<C> -> {
            withRetry {
                modalSheetState.animateTo(SheetDetent.Companion.Hidden)
                modalSheetState.animateTo(targetDetent)
            }
            childContent.value = { content(logicEvent.instance) }
        }

        is LogicEvent.Open<C> -> {
            modalSheetState.animateTo(targetDetent)
            childContent.value = { content(logicEvent.instance) }
        }

        LogicEvent.Unhandled -> Unit
    }
}

@Suppress("LambdaParameterInRestartableEffect", "ContentSlotReused")
@Composable
fun <C : Any> ModalBottomSheetSlot(
    instance: C?,
    onDismiss: () -> Unit,
    initialDetent: SheetDetent = SheetDetent.Companion.Hidden,
    targetDetent: SheetDetent = SheetDetent.FullyExpanded,
    detents: List<SheetDetent> = listOf(SheetDetent.Hidden, SheetDetent.FullyExpanded),
    content: @Composable ModalBottomSheetScope.(C) -> Unit
) {
    val childContent = remember { mutableStateOf(emptyContent) }

    val modalSheetState = rememberModalBottomSheetState(
        initialDetent = initialDetent,
        detents = detents
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
    LaunchedEffect(instance, modalSheetState, childContent.value) {
        getLogicEventFlow(
            instance = instance,
            modalSheetState = modalSheetState
        ).onEach { logicEvent ->
            handleLogicEvent(
                logicEvent = logicEvent,
                modalSheetState = modalSheetState,
                childContent = childContent,
                content = content,
                targetDetent = targetDetent
            )
        }.collect()
    }
    ModalBottomSheet(
        state = modalSheetState,
        content = { childContent.value.invoke(this) }
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
