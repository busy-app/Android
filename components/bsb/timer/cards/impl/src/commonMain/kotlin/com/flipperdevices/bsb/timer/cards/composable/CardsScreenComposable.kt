package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.flipperdevices.bsb.timer.cards.model.EXAMPLE_DATA
import com.flipperdevices.bsb.timer.cards.model.LayoutOffsetData
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardsScreenComposable() {
    val items = EXAMPLE_DATA
    val layoutOffsetDataState = remember { mutableStateOf(LayoutOffsetData()) }
    val pagerState = rememberPagerState { items.size }

    CardsVideoComposable(
        pagerState = pagerState,
        items = items,
        layoutOffsetDataState = layoutOffsetDataState
    )

    CardsCardSelectionComposable(
        layoutOffsetDataState = layoutOffsetDataState,
        currentData = items[pagerState.currentPage],
        pagerState = pagerState
    )
}
