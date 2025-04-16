package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet

internal fun Modifier.wormTransition(
    pagerState: PagerState,
    color: Color,
    spacing: Dp,
) = drawBehind {
    val distance = size.width + spacing.roundToPx()
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    val wormOffset = (scrollPosition % 1) * 2

    val xPos = scrollPosition.toInt() * distance
    val head = xPos + distance * 0f.coerceAtLeast(wormOffset - 1)
    val tail = xPos + size.width + 1f.coerceAtMost(wormOffset) * distance

    val worm = RoundRect(
        left = head,
        top = 0f,
        right = tail,
        bottom = size.height,
        cornerRadius = CornerRadius(50f)
    )

    val path = Path().apply {
        addRoundRect(worm)
    }
    drawPath(path = path, color = color)
}

@Composable
internal fun WormDotPagerIndicator(
    count: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    size: Dp = 10.dp,
    spacing: Dp = 10.dp,
    activeDotColor: Color = LocalCorruptedPallet.current.neutral.senary,
    inactiveDotColor: Color = LocalCorruptedPallet.current.neutral.quaternary
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier
                .height(size.times(2)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(count) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(
                            color = inactiveDotColor,
                            shape = CircleShape
                        )
                )
            }
        }

        Box(
            Modifier
                .wormTransition(pagerState, activeDotColor, spacing)
                .size(size)
        )
    }
}