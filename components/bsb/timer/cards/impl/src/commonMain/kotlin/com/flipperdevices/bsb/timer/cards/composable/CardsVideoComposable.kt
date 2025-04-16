package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_three_dots
import com.flipperdevices.bsb.timer.cards.model.LayoutOffsetData
import com.flipperdevices.bsb.timer.cards.model.PagerData
import com.flipperdevices.ui.video.BSBVideoPlayer
import kotlinx.collections.immutable.ImmutableList

private const val SQUARE_RATIO = 1f / 1f

@Composable
internal fun CardsVideoComposable(
    pagerState: PagerState,
    items: ImmutableList<PagerData>,
    layoutOffsetDataState: MutableState<LayoutOffsetData>
) {
    val localDensity: Density = LocalDensity.current
    val topPadding by animateDpAsState(
        layoutOffsetDataState.value.titleOffsetYDp.plus(layoutOffsetDataState.value.titleHeightDp)
    )
    HorizontalPager(state = pagerState) { page ->
        val data = items[page]
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = topPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            BSBVideoPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .aspectRatio(SQUARE_RATIO)
                    .onGloballyPositioned { layoutCoordinates ->
                        layoutOffsetDataState.value = layoutOffsetDataState.value.copy(
                            videoHeightDp = with(localDensity) {
                                layoutCoordinates.size.height.toDp()
                            }
                        )
                    },
                uri = data.videoUri,
                firstFrame = Res.drawable.ic_three_dots // todo
            )
        }
    }
}
