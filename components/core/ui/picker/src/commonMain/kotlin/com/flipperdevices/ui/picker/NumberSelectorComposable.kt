package com.flipperdevices.ui.picker

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlin.math.absoluteValue

@Composable
@Suppress("LongMethod")
fun NumberSelectorComposable(
    numberSelectorState: NumberSelectorState,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    postfix: String? = null
) {
    val pagerState = numberSelectorState.rememberPagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { isScrollInProgress -> !isScrollInProgress }
            .drop(1)
            .collect {
                val page = pagerState.currentPage.mod(numberSelectorState.maxPages)
                val value = numberSelectorState.toValue(page)
                onValueChange.invoke(value)
            }
    }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = NoLimitPagerSnapDistance
    )
    var contentHeightDp by remember { mutableStateOf<Dp?>(null) }
    var contentWidthDp by remember { mutableStateOf<Dp?>(null) }
    val backgroundColor = MaterialTheme.colors.background
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val contentPadding = contentHeightDp?.let {
            (maxHeight - it) / 2
        } ?: 0.dp
        VerticalPager(
            modifier = Modifier.wrapContentHeight()
                .align(Alignment.Center)
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    @Suppress("MagicNumber")
                    val brush = Brush.verticalGradient(
                        0f to backgroundColor.copy(alpha = 0f),
                        0.1f to backgroundColor.copy(alpha = 0f),
                        0.2f to backgroundColor.copy(alpha = 0.3f),
                        0.5f to backgroundColor.copy(alpha = 1f),
                        0.8f to backgroundColor.copy(alpha = 0.3f),
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
            val activeColor = LocalCorruptedPallet.current.white.invert
            val inactiveColor = LocalCorruptedPallet.current.white.invert
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
                    contentWidthDp = with(localDensity) { coordinates.size.width.toDp() }
                },
                number = numberSelectorState.toValue(page.mod(numberSelectorState.maxPages)),
                color = textColor
            )
        }

        contentWidthDp?.let { contentWidthDp ->
            postfix?.let {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = contentWidthDp / 2),
                    text = postfix,
                    fontSize = 24.sp,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontWeight = FontWeight.W500,
                    fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                    textAlign = TextAlign.Center
                )
            }
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
                    .width(48.dp)
                    .wrapContentHeight(
                        align = Alignment.CenterVertically, // aligns to the center vertically (default value)
                        unbounded = true // Makes sense if the container size less than text's height
                    ),
                text = symbol.toString(),
                fontSize = 64.sp,
                color = color,
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                textAlign = TextAlign.Center
            )
        }
    }
}

class NumberSelectorState(
    val intProgression: IntProgression,
    val initialValue: Int,
) {
    val maxPages: Int = intProgression.last.div(intProgression.step)
    val initialPage: Int = initialValue.div(intProgression.step)
    fun toPage(value: Int) = value.div(intProgression.step)
    fun toValue(page: Int) = page.times(intProgression.step)
}

@Composable
fun NumberSelectorState.rememberPagerState(): PagerState {
    val pagerSize = Int.MAX_VALUE - 1
    val initialPage = remember(
        key1 = pagerSize,
        key2 = initialValue,
        key3 = maxPages,
        calculation = {
            (pagerSize / 2).floorDiv(maxPages) * maxPages + initialPage
        }
    )
    return rememberPagerState(
        initialPage = initialPage,
        pageCount = {
            pagerSize
        }
    )
}

@Composable
fun rememberTimerState(
    intProgression: IntProgression,
    initialValue: Int = intProgression.first
): NumberSelectorState {
    val fixedIntProgression = remember(intProgression) {
        intProgression.first..intProgression.last.plus(intProgression.step) step intProgression.step
    }
    return remember(fixedIntProgression, initialValue) {
        NumberSelectorState(
            intProgression = fixedIntProgression,
            initialValue = initialValue
        )
    }
}
