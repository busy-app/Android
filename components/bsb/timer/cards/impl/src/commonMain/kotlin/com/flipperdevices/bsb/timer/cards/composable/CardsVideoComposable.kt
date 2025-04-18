package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_three_dots
import com.flipperdevices.bsb.timer.cards.api.LocalAnimatedVisibilityScope
import com.flipperdevices.bsb.timer.cards.api.LocalSharedTransitionScope
import com.flipperdevices.bsb.timer.cards.model.LocalVideoLayoutInfo
import com.flipperdevices.bsb.timer.cards.model.PagerData
import com.flipperdevices.ui.video.BSBVideoPlayer
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.DrawableResource

internal const val SQUARE_RATIO = 1f / 1f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun CardsVideoComposable(
    pagerState: PagerState,
    items: ImmutableList<PagerData>,
) {
    val videoLayoutInfo = LocalVideoLayoutInfo.current
    val density = LocalDensity.current
    with(LocalSharedTransitionScope.current) {
        Box(
            Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "video"),
                animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                enter = fadeIn(),
                exit = fadeOut(),
                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
            )
        ) {
            HorizontalPager(
                state = pagerState,
            ) { page ->
                val data = items[page]
                BSBVideoPlayer(
                    modifier = Modifier
                        .zIndex(-1f)
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(top = animateDpAsState(LocalVideoLayoutInfo.current.videoTopOffsetDp).value)
                        .padding(horizontal = 32.dp)
                        .aspectRatio(SQUARE_RATIO)
                        .onGloballyPositioned { layoutCoordinates ->
                            val heightDp = with(density) { layoutCoordinates.size.height.toDp() }
                            videoLayoutInfo.setVideoHeightDp(heightDp)
                        },
                    uri = data.videoUri,
                    firstFrame = data.firstFrame
                )
            }
        }
    }
}
