package com.flipperdevices.ui.timeline


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.compose.ui.tooling.preview.Preview


/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
internal fun Int.formattedTime(): String {
    return if (this < 10) "0$this" else "$this"
}

internal fun Duration.formattedTime(): String {
    return this.toComponents { days, hours, minutes, seconds, nanoseconds ->
        when {
            days > 0 -> "${days}d ${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            minutes > 0 -> "${minutes}m ${seconds.formattedTime()}s"
            seconds == 0 -> "∞"
            else -> "${seconds}s"
        }
    }
}

private val Number.sdp: Dp
    @Composable
    get() {
        return this.toFloat().dp
    }

/**
 * A customizable wheel picker component for Android Jetpack Compose.
 *
 * The `HorizontalWheelPicker` allows users to select an item using a list of vertical lines displayed horizontally
 * as vertical lines. The selected item is highlighted, and surrounding items can be
 * customized with different heights, colors, and transparency effects. The component
 * supports dynamic width, customizable item spacing, and rounded corners.
 *
 * @param modifier The modifier to be applied to the `WheelPicker` component.
 * @param wheelPickerWidth The width of the entire picker. If null, the picker will use the full screen width. Default is `null`.
 * @param totalItems The total number of items in the picker.
 * @param initialSelectedItem The index of the item that is initially selected.
 * @param lineWidth The width of each vertical line in the picker. Default is `2.sdp`.
 * @param selectedLineHeight The height of the selected item (line) in the picker. Default is `64.sdp`.
 * @param multipleOfFiveLineHeight The height of lines at indices that are multiples of 5. Default is `40.sdp`.
 * @param normalLineHeight The height of lines that are not the selected item or multiples of 5. Default is `30.sdp`.
 * @param selectedMultipleOfFiveLinePaddingBottom The padding bottom for the selected item (line) that are multiples of 5. Default is `0.sdp`.
 * @param normalMultipleOfFiveLinePaddingBottom The padding bottom for lines that are multiples of 5. Default is `6.sdp`.
 * @param normalLinePaddingBottom The padding bottom for normal lines. Default is `8.sdp`.
 * @param lineSpacing The spacing between each line in the picker. Default is `8.sdp`.
 * @param lineRoundedCorners The corner radius of each vertical line, applied to create rounded corners. Default is `2.sdp`.
 * @param selectedLineColor The color of the selected item (line) in the picker. Default is `Color(0xFF00D1FF)`.
 * @param unselectedLineColor The color of the unselected items (lines) in the picker. Default is `Color.LightGray`.
 * @param fadeOutLinesCount The number of lines at the edges of the picker that will gradually fade out. Default is `4`.
 * @param maxFadeTransparency The maximum transparency level applied to the fading lines. Default is `0.7f`.
 * @param onItemSelected A callback function invoked when a new item is selected, passing the selected index as a parameter.
 *
 */
@Composable
fun BoxWithConstraintsScope.HorizontalWheelPicker(
    modifier: Modifier = Modifier,
    wheelPickerWidth: Dp? = null,
    totalItems: Int,
    initialSelectedItem: Int,
    lineWidth: Dp = 4.sdp,
    selectedLineWidth: Dp = 8.dp,
    selectedLineHeight: Dp = 64.sdp,
    multipleOfFiveLineHeight: Dp = 40.sdp,
    normalLineHeight: Dp = 30.sdp,
    selectedMultipleOfFiveLinePaddingBottom: Dp = 0.sdp,
    normalMultipleOfFiveLinePaddingBottom: Dp = 6.sdp,
    normalLinePaddingBottom: Dp = 8.sdp,
    lineSpacing: Dp = 8.sdp,
    lineRoundedCorners: Dp = 2.sdp,
    selectedLineColor: Color = LocalPallet.current.white.invert,
    unselectedLineColor: Color = LocalPallet.current.white.invert.copy(0.2f),
    fadeOutLinesCount: Int = 4,
    maxFadeTransparency: Float = 0.7f,
    onItemSelected: (Int) -> Unit
) {
    val density = LocalDensity.current.density
    val screenWidthDp = (with(LocalDensity.current) { maxWidth.toPx() } / density).dp
    val effectiveWidth = wheelPickerWidth ?: screenWidthDp

    var currentSelectedItem by remember { mutableIntStateOf(initialSelectedItem) }
    var isIndexUpdateBlocked by remember { mutableStateOf(false) }

    val scrollState: LazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialSelectedItem)
    val textScrollState = rememberLazyListState(initialFirstVisibleItemIndex = initialSelectedItem)

    val visibleItemsInfo by remember { derivedStateOf { scrollState.layoutInfo.visibleItemsInfo } }
    val firstVisibleItemIndex = visibleItemsInfo.firstOrNull()?.index ?: -1
    val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: -1
    val totalVisibleItems = lastVisibleItemIndex - firstVisibleItemIndex + 1
    val middleIndex = firstVisibleItemIndex + totalVisibleItems / 2
    val bufferIndices = totalVisibleItems / 2

    LaunchedEffect(middleIndex, currentSelectedItem, scrollState.isScrollInProgress) {
        onItemSelected(currentSelectedItem)

        val mod = currentSelectedItem % 10
        val div = currentSelectedItem / 10
        val result = if (mod < (10 / 2)) div.times(10) else div.times(10) + 10
        if (!scrollState.isScrollInProgress) {
            scrollState.animateScrollToItem(result)
        }
    }
    LaunchedEffect(currentSelectedItem, scrollState) {
        val nonAdjustedIndex = currentSelectedItem + bufferIndices
        textScrollState.animateScrollToItem(nonAdjustedIndex)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LazyRow(
            modifier = modifier.width(effectiveWidth),
            state = scrollState,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(totalItems + totalVisibleItems) { index ->
                val adjustedIndex = index - bufferIndices

                if (index == middleIndex && !isIndexUpdateBlocked) {
                    currentSelectedItem = adjustedIndex
                }

                val lineHeight by animateDpAsState(
                    when {
                        index == middleIndex -> selectedLineHeight
                        adjustedIndex % 5 == 0 -> multipleOfFiveLineHeight
                        else -> normalLineHeight
                    },
                    tween(600)
                )

                val paddingBottom by animateDpAsState(
                    targetValue = when {
                        index == middleIndex -> selectedMultipleOfFiveLinePaddingBottom
                        adjustedIndex % 5 == 0 -> normalMultipleOfFiveLinePaddingBottom
                        else -> normalLinePaddingBottom
                    },
                    animationSpec = tween(400)
                )

                val lineTransparency = animateFloatAsState(
                    calculateLineTransparency(
                        index,
                        totalItems,
                        bufferIndices,
                        firstVisibleItemIndex,
                        lastVisibleItemIndex,
                        fadeOutLinesCount,
                        maxFadeTransparency
                    ),
                    tween(600)
                )

                VerticalLine(
                    i = adjustedIndex,
                    lineWidth = lineWidth,
                    selectedLineWidth = selectedLineWidth,
                    lineHeight = lineHeight,
                    paddingBottom = paddingBottom,
                    roundedCorners = lineRoundedCorners,
                    isSelected = index == middleIndex,
                    lineTransparency = lineTransparency.value,
                    selectedLineColor = selectedLineColor,
                    unselectedLineColor = unselectedLineColor,
                    maxLineHeight = selectedLineHeight
                )


                Spacer(modifier = Modifier.width(lineSpacing))
            }
        }
    }

}

/**
 * A composable function that renders a single vertical line in the `WheelPicker`.
 *
 * The `VerticalLine` component is used within the `WheelPicker` to represent each
 * selectable item as a vertical line. The line's appearance can be customized with
 * different heights, padding, rounded corners, colors, and transparency effects.
 *
 * @param lineWidth The width of the vertical line.
 * @param lineHeight The height of the vertical line.
 * @param paddingBottom The padding applied to the bottom of the line.
 * @param roundedCorners The corner radius applied to the line, creating rounded corners.
 * @param isSelected A boolean flag indicating if the line is at the center (selected item).
 * @param lineTransparency The transparency level applied to the line.
 * @param selectedLineColor The color of the line if it is the selected item.
 * @param unselectedLineColor The color of the line if it is not the selected item.
 *
 */
@Composable
private fun VerticalLine(
    i: Int,
    lineWidth: Dp,
    selectedLineWidth: Dp,
    lineHeight: Dp,
    paddingBottom: Dp,
    roundedCorners: Dp,
    isSelected: Boolean,
    lineTransparency: Float,
    selectedLineColor: Color,
    unselectedLineColor: Color,
    maxLineHeight: Dp
) {
    val textMeasurer = rememberTextMeasurer()
    val fontSizeFloat by animateFloatAsState(
        targetValue = when {
            isSelected && i == 0 -> 50f
            isSelected -> 40f
            else -> 15f
        },
        animationSpec = tween()
    )
    val fontColor by animateColorAsState(
        targetValue = when {
            isSelected -> LocalPallet.current
                .white
                .invert

            else -> LocalPallet.current
                .white
                .invert
                .copy(0.2f)
        }
    )
    val textColor by animateColorAsState(
        fontColor.copy(
            alpha = when {
                i % 10 == 0 -> 1f
                (isSelected && i % 5 == 0) -> 1f
                else -> 0f
            }.coerceAtMost(fontColor.alpha)
        )
    )
    val result = remember(isSelected,textColor, fontSizeFloat, fontColor, i) {
        textMeasurer.measure(
            text = i.seconds.formattedTime(),
            style = TextStyle(
                fontSize = fontSizeFloat.sp,
                color = textColor,
                textAlign = TextAlign.Center
            ),
            overflow = TextOverflow.Clip
        )
    }

    val textYOffset by animateFloatAsState(
        if (isSelected) -result.size.height.div(2).toFloat()
        else result.size.height.div(2).toFloat()
    )
    val color by animateColorAsState(
        if (isSelected) selectedLineColor else unselectedLineColor,
        tween(600)
    )
    val width by animateDpAsState(
        if (isSelected) selectedLineWidth else lineWidth,
        tween(600)
    )
    val localDensity = LocalDensity.current

    Canvas(
        Modifier
            .width(1.dp)
            .height(maxLineHeight.plus(with(localDensity) { result.size.height.toDp().times(2) }))
    ) {
        if (i % 5 == 0) {
            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = -result.size.width.div(2).toFloat().plus(result.size.width % 2),
                    y = textYOffset
                )
            )
        }

        drawRect(
            topLeft = Offset(
                x = -with(localDensity) { width.toPx() } / 2,
                y = with(localDensity) { paddingBottom.toPx() }
                    .plus(result.size.height * 2)
            ),
            color = color.copy(alpha = lineTransparency.coerceAtMost(color.alpha)),
            size = Size(
                width = with(localDensity) { width.toPx() },
                height = with(localDensity) { lineHeight.toPx() }),
        )
    }
}

/**
 * Calculates the transparency level for a line based on its position within the `WheelPicker`.
 *
 * This function determines the transparency level for a line in the `WheelPicker` based on
 * its index and its position relative to the visible items in the list. The transparency
 * gradually increases towards the edges of the picker, creating a fade-out effect.
 *
 * @param lineIndex The index of the current line being rendered.
 * @param totalLines The total number of lines in the picker.
 * @param bufferIndices The number of extra indices used for rendering outside the visible area.
 * @param firstVisibleItemIndex The index of the first visible item in the list.
 * @param lastVisibleItemIndex The index of the last visible item in the list.
 * @param fadeOutLinesCount The number of lines that should gradually fade out at the edges.
 * @param maxFadeTransparency The maximum transparency level to apply during the fade-out effect.
 * @return A `Float` value representing the calculated transparency level for the line.
 *
 */
private fun calculateLineTransparency(
    lineIndex: Int,
    totalLines: Int,
    bufferIndices: Int,
    firstVisibleItemIndex: Int,
    lastVisibleItemIndex: Int,
    fadeOutLinesCount: Int,
    maxFadeTransparency: Float
): Float {
    val actualCount = fadeOutLinesCount + 1
    val transparencyStep = maxFadeTransparency / actualCount

    return when {
        lineIndex < bufferIndices || lineIndex > (totalLines + bufferIndices) -> 0.0f
        lineIndex in firstVisibleItemIndex until firstVisibleItemIndex + fadeOutLinesCount -> {
            transparencyStep * (lineIndex - firstVisibleItemIndex + 1)
        }

        lineIndex in (lastVisibleItemIndex - fadeOutLinesCount + 1)..lastVisibleItemIndex -> {
            transparencyStep * (lastVisibleItemIndex - lineIndex + 1)
        }

        else -> 1.0f
    }
}

@Preview
@Composable
fun HorizontalWheelPickerPreview() {
    BusyBarThemeInternal {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var selectedItem by remember { mutableIntStateOf(15) }
                Text(
                    text = selectedItem.seconds.formattedTime(),
                    fontSize = 46.sp,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    HorizontalWheelPicker(
                        totalItems = 24.hours.inWholeSeconds.toInt(),
                        initialSelectedItem = selectedItem,
                        onItemSelected = { item ->
                            selectedItem = item
                        }
                    )
                }
            }
        }
    }
}