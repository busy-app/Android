package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalDragOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.timer.cards.composable.State.*
import com.flipperdevices.bsb.timer.cards.model.EXAMPLE_DATA
import com.flipperdevices.bsb.timer.cards.model.LayoutOffsetData
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlin.math.absoluteValue
import kotlinx.coroutines.awaitCancellation
import org.jetbrains.compose.resources.ExperimentalResourceApi

sealed interface State {
    data object Selecting : State
    data object Modal : State
    data class Renaming(val name: String) : State
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardsScreenComposable() {
    val items = EXAMPLE_DATA
    val layoutOffsetDataState = remember { mutableStateOf(LayoutOffsetData()) }
    val pagerState = rememberPagerState { items.size }
    var state = remember { mutableStateOf<State>(State.Selecting) }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, offset ->
                    println(offset)
                    if (offset.absoluteValue > 10) state.value = Modal
                }
            }
    ) {
        CardsVideoComposable(
            pagerState = pagerState,
            items = items,
            layoutOffsetDataState = layoutOffsetDataState
        )
        Crossfade(state.value) { localState ->
            when (val localState = localState) {
                is Renaming -> {
                    EditCardNameComposable(
                        layoutOffsetDataState = layoutOffsetDataState,
                        name = localState.name,
                        onFinish = {
                            state.value = Selecting
                        },
                        onNameChange = {
                            state.value = localState.copy(name = it)
                        }
                    )
                }

                Selecting -> {
                    CardsCardSelectionComposable(
                        layoutOffsetDataState = layoutOffsetDataState,
                        currentData = items[pagerState.currentPage],
                        pagerState = pagerState,
                        onNameClick = {
                            state.value = Renaming(items[pagerState.currentPage].timerSettings.name)
                        },
                        onOptionsClick = {
                            state.value = Modal
                        }
                    )
                }

                Modal -> Unit
            }
        }
    }
    val halfExpandedDetent = SheetDetent("half") { containerHeight, sheetHeight ->
        (containerHeight / 3).coerceAtMost(containerHeight).coerceAtMost(sheetHeight)
    }
    ModalBottomSheetSlot(
        instance = state.value as? State.Modal,
        onDismiss = {
            state.value = State.Selecting
        },
        detents = listOf(
            SheetDetent.Hidden,
            halfExpandedDetent,
            SheetDetent.FullyExpanded
        ),
        targetDetent = halfExpandedDetent,
        content = {
            Sheet(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .statusBarsPadding()
                    .padding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal).asPaddingValues()
                    )
                    .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color(color = 0xFF1E1E1E)) // todo
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .imePadding(),
            ) {

                Column {
                    DragIndication()
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(10) {
                            Box(Modifier.fillMaxWidth().height(200.dp).background(Color.Green))
                        }
                    }
                }
            }
        }
    )
}
