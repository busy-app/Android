package com.flipperdevices.ui.timeline


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.compose.ui.tooling.preview.Preview


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
 * @param onItemSelected A callback function invoked when a new item is selected, passing the selected index as a parameter.
 *
 */
@Composable
fun BoxWithConstraintsScope.HorizontalWheelPicker(
    modifier: Modifier = Modifier,
    wheelPickerWidth: Dp? = null,
    totalItems: Int,
    initialSelectedItem: Int,
    onItemSelected: (Int) -> Unit,
    lineStyle: LineStyle = LineStyle.Default
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
        val step = 5
        val mod = currentSelectedItem % step
        val div = currentSelectedItem / step
        val result = if (mod < (step / 2)) div.times(step) else div.times(step) + step
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
                val isSelected = index == middleIndex

                if (isSelected && !isIndexUpdateBlocked) {
                    currentSelectedItem = adjustedIndex
                }


                val lineTransparency = animateFloatAsState(
                    calculateLineTransparency(
                        index,
                        totalItems,
                        bufferIndices,
                        firstVisibleItemIndex,
                        lastVisibleItemIndex,
                        lineStyle.fadeOutLinesCount,
                        lineStyle.maxFadeTransparency
                    ),
                    tween(600)
                )

                VerticalLine(
                    index = adjustedIndex,
                    isSelected = index == middleIndex,
                    lineTransparency = lineTransparency.value,
                    style = lineStyle,
                )
                Spacer(modifier = Modifier.width(lineStyle.lineSpacing))
            }
        }
    }
}


@Preview
@Composable
fun HorizontalWheelPickerPreview(
    style: LineStyle? = null
) {
    BusyBarThemeInternal {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var selectedItem by remember { mutableIntStateOf(15) }
                Spacer(modifier = Modifier.height(8.dp))
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    HorizontalWheelPicker(
                        lineStyle = style ?: LineStyle.Default,
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