package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.cards.composable.CardsCardSelectionComposable
import com.flipperdevices.bsb.timer.cards.composable.CardsVideoComposable
import com.flipperdevices.bsb.timer.cards.composable.EditCardNameComposable
import com.flipperdevices.bsb.timer.cards.composable.State
import com.flipperdevices.bsb.timer.cards.composable.State.Modal
import com.flipperdevices.bsb.timer.cards.composable.State.Renaming
import com.flipperdevices.bsb.timer.cards.composable.State.Selecting
import com.flipperdevices.bsb.timer.cards.model.EXAMPLE_DATA
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import kotlin.math.absoluteValue
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PagerDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: DecomposeOnBackParameter,
    @Assisted private val onRename: (CardRenameItem) -> Unit,
    @Assisted private val onSettings: () -> Unit,
) : ScreenDecomposeComponent(componentContext) {

    @Composable
    override fun Render(modifier: Modifier) {
        val items = EXAMPLE_DATA
        val pagerState = rememberPagerState { items.size }

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, offset ->
                        println(offset)
                        if (offset.absoluteValue > 10) {

                        }
                    }
                }
        ) {
            CardsVideoComposable(
                pagerState = pagerState,
                items = items,
            )
            CardsCardSelectionComposable(
                currentData = items[pagerState.currentPage],
                pagerState = pagerState,
                onNameClick = {
                    val item = items[pagerState.currentPage]
                    val cardRenameItem = CardRenameItem(
                        videoUri = item.videoUri,
                        name = item.timerSettings.name,
                        id = item.timerSettings.id
                    )
                    onRename.invoke(cardRenameItem)
                },
                onOptionsClick = {
                    onSettings.invoke()
                }
            )
        }
    }
}