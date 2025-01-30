package com.flipperdevices.ui.picker

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.math.absoluteValue

@Composable
@Suppress("LongMethod")
fun NumberSelectorComposable(
    count: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = NoLimitPagerSnapDistance
    )
    var contentHeightDp by remember { mutableStateOf<Dp?>(null) }
    val backgroundColor = MaterialTheme.colors.background
    BoxWithConstraints(modifier = modifier) {
        val contentPadding = contentHeightDp?.let {
            (maxHeight - it) / 2
        } ?: 0.dp
        VerticalPager(
            modifier = Modifier.wrapContentHeight()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    @Suppress("MagicNumber")
                    val brush = Brush.verticalGradient(
                        0f to backgroundColor.copy(alpha = 0f),
                        0.1f to backgroundColor.copy(alpha = 0f),
                        0.3f to backgroundColor.copy(alpha = 0.3f),
                        0.5f to backgroundColor.copy(alpha = 1f),
                        0.7f to backgroundColor.copy(alpha = 0.3f),
                        0.9f to backgroundColor.copy(alpha = 0f),
                        1f to backgroundColor.copy(alpha = 0f),
                    )
                    drawRect(brush = brush, blendMode = BlendMode.DstIn)
                },
            state = pagerState,
            contentPadding = PaddingValues(vertical = contentPadding),
            flingBehavior = fling,
            horizontalAlignment = Alignment.End
        ) { page ->
            val activeColor = LocalPallet.current.white.invert
            val inactiveColor = LocalPallet.current.white.invert
            val textColor = remember(
                activeColor,
                inactiveColor,
                pagerState.currentPage,
                page,
                pagerState.currentPageOffsetFraction
            ) {
                val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue
                lerp(
                    start = activeColor,
                    stop = inactiveColor,
                    fraction = pageOffset.coerceIn(0f, 1f)
                )
            }

            val localDensity = LocalDensity.current
            NumberElementComposable(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    contentHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                },
                number = page.mod(count),
                color = textColor
            )
        }
    }
}

private const val MIN_TWO_DIGIT_VALUE = 10

@Composable
private fun NumberElementComposable(
    number: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val numberText = if (number < MIN_TWO_DIGIT_VALUE) {
        "0$number"
    } else {
        number.toString()
    }
    Row(
        modifier = modifier
    ) {
        numberText.forEach { symbol ->
            Text(
                modifier = Modifier
                    .width(64.dp)
                    .wrapContentHeight(
                        align = Alignment.CenterVertically, // aligns to the center vertically (default value)
                        unbounded = true // Makes sense if the container size less than text's height
                    ),
                text = symbol.toString(),
                fontSize = 100.sp,
                color = color,
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun rememberTimerState(count: Int, initialNumber: Int): PagerState {
    val pagerSize = Int.MAX_VALUE - 1
    val initialPage = remember(
        pagerSize,
        initialNumber,
        count
    ) {
        (pagerSize / 2).floorDiv(count) * count + initialNumber
    }
    return rememberPagerState(
        initialPage = initialPage,
        pageCount = {
            pagerSize
        }
    )
}
