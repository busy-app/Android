package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.flipperdevices.bsb.timer.cards.model.EXAMPLE_DATA
import com.flipperdevices.bsb.timer.cards.model.LayoutOffsetData
import org.jetbrains.compose.resources.ExperimentalResourceApi

sealed interface State {
    data object Selecting : State
    data class Renaming(val name: String) : State
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardsScreenComposable() {
    val items = EXAMPLE_DATA
    val layoutOffsetDataState = remember { mutableStateOf(LayoutOffsetData()) }
    val pagerState = rememberPagerState { items.size }
    var state = remember { mutableStateOf<State>(State.Selecting) }

    CardsVideoComposable(
        pagerState = pagerState,
        items = items,
        layoutOffsetDataState = layoutOffsetDataState
    )
//    Crossfade(state.value) { localState ->
    when (val localState = state.value) {
        is State.Renaming -> {
            EditCardNameComposable(
                layoutOffsetDataState = layoutOffsetDataState,
                name = localState.name,
                onFinish = {
                    state.value = State.Selecting
                },
                onNameChange = {
                    state.value = localState.copy(name = it)
                }
            )
        }

        State.Selecting -> {
            CardsCardSelectionComposable(
                layoutOffsetDataState = layoutOffsetDataState,
                currentData = items[pagerState.currentPage],
                pagerState = pagerState,
                onNameClick = {
                    state.value = State.Renaming(items[pagerState.currentPage].timerSettings.name)
                }
            )
        }
//        }
    }
}
