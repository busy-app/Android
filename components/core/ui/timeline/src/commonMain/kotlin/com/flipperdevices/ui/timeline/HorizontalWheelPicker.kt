package com.flipperdevices.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.ui.timeline.model.PickerLineStyle
import com.flipperdevices.ui.timeline.model.VisibleLinesState
import com.flipperdevices.ui.timeline.util.toDuration
import com.flipperdevices.ui.timeline.util.toFormattedTime
import com.flipperdevices.ui.timeline.util.toLong
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

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
 * @param onItemSelect A callback function invoked when a new item is selected, passing the selected index as a parameter.
 *
 */
@Suppress("MaxLineLength", "LongMethod", "LambdaParameterInRestartableEffect")
@Composable
fun BoxWithConstraintsScope.HorizontalWheelPicker(
    totalItems: Int,
    initialSelectedItem: Int,
    transform: (Int) -> String,
    onItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isByProgress: Boolean = true,
    wheelPickerWidth: Dp? = null,
    lineStyle: PickerLineStyle = PickerLineStyle.Default
) {
    val effectiveWidth = wheelPickerWidth ?: maxWidth

    var currentSelectedItem by remember { mutableIntStateOf(initialSelectedItem) }
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = initialSelectedItem)

    val visibleItemsInfo by remember {
        derivedStateOf {
            val visibleItemsInfo = scrollState.layoutInfo.visibleItemsInfo
            VisibleLinesState(
                firstVisibleItemIndex = visibleItemsInfo.firstOrNull()?.index ?: -1,
                lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: -1
            )
        }
    }
    LaunchedEffect(visibleItemsInfo, currentSelectedItem, scrollState.isScrollInProgress) {
        onItemSelect(currentSelectedItem)
        val step = lineStyle.step
        val mod = currentSelectedItem % step
        val div = currentSelectedItem / step
        val result = if (mod < (step / 2)) div.times(step) else div.times(step) + step
        if (result == currentSelectedItem) return@LaunchedEffect
        if (!scrollState.isScrollInProgress) {
            scrollState.animateScrollToItem(result)
        }
    }

    LaunchedEffect(Unit) {
        scrollState.animateScrollToItem(initialSelectedItem)
    }

    LazyRow(
        modifier = modifier.width(effectiveWidth),
        state = scrollState,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(
            count = totalItems + visibleItemsInfo.totalVisibleItems,
            key = { index -> index - visibleItemsInfo.bufferIndices },
            itemContent = { index ->
                val adjustedIndex = index - visibleItemsInfo.bufferIndices

                if (index == visibleItemsInfo.middleIndex) {
                    currentSelectedItem = adjustedIndex
                }

                val lineTransparency = calculateLineTransparency(
                    lineIndex = index,
                    totalLines = totalItems,
                    bufferIndices = visibleItemsInfo.bufferIndices,
                    firstVisibleItemIndex = visibleItemsInfo.firstVisibleItemIndex,
                    lastVisibleItemIndex = visibleItemsInfo.lastVisibleItemIndex,
                    fadeOutLinesCount = lineStyle.fadeOutLinesCount,
                    maxFadeTransparency = lineStyle.maxFadeTransparency
                )

                val isGone =
                    index < visibleItemsInfo.bufferIndices || index > (totalItems + visibleItemsInfo.bufferIndices)
                VerticalLine(
                    index = index,
                    adjustedIndex = adjustedIndex,
                    lineStyle = lineStyle,
                    indexAtCenter = index == visibleItemsInfo.middleIndex,
                    middleIndex = visibleItemsInfo.middleIndex,
                    lineTransparency = lineTransparency,
                    transform = transform,
                    isVisible = !isGone,
                    isByProgress = isByProgress
                )

                Spacer(modifier = Modifier.width(lineStyle.lineSpacing))
            }
        )
    }
}

@Composable
fun BoxWithConstraintsScope.HorizontalWheelPicker(
    durationUnit: DurationUnit,
    initialSelectedItem: Duration,
    progression: IntProgression,
    onItemSelect: (Duration) -> Unit,
    modifier: Modifier = Modifier,
    lineStyle: PickerLineStyle = PickerLineStyle.Default,
    isByProgress: Boolean = true,
) {
    val firstDuration = durationUnit.toDuration(progression.first)

    HorizontalWheelPicker(
        modifier = modifier,
        isByProgress = isByProgress,
        wheelPickerWidth = null,
        totalItems = progression.last - progression.first,
        initialSelectedItem = durationUnit
            .toLong(initialSelectedItem.minus(firstDuration))
            .coerceAtLeast(0L)
            .toInt(),
        transform = { value ->
            durationUnit.toDuration(value).plus(firstDuration).toFormattedTime()
        },
        lineStyle = lineStyle,
        onItemSelect = { value ->
            val duration = durationUnit.toDuration(value).plus(firstDuration)
            if (durationUnit.toLong(duration) % progression.step == 0L) {
                onItemSelect.invoke(duration)
            }
        },
    )
}

@Preview
@Composable
private fun HorizontalWheelPickerPreview() {
    BusyBarThemeInternal {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                HorizontalWheelPicker(
                    progression = IntProgression.fromClosedRange(
                        rangeStart = 15.minutes.inWholeMinutes.toInt(),
                        rangeEnd = 3.hours.inWholeMinutes.toInt(),
                        step = 5.minutes.inWholeMinutes.toInt()
                    ),
                    initialSelectedItem = 10.minutes,
                    onItemSelect = { _ -> },
                    durationUnit = DurationUnit.MINUTES,
                    isByProgress = true
                )
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                HorizontalWheelPicker(
                    progression = IntProgression.fromClosedRange(
                        rangeStart = 15.minutes.inWholeMinutes.toInt(),
                        rangeEnd = 3.hours.inWholeMinutes.toInt(),
                        step = 5.minutes.inWholeMinutes.toInt()
                    ),
                    initialSelectedItem = 10.minutes,
                    onItemSelect = { _ -> },
                    durationUnit = DurationUnit.MINUTES,
                    isByProgress = false
                )
            }
        }
    }
}
