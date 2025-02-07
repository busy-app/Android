package com.flipperdevices.ui.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch

internal val BarWidth = 4.dp
internal val BarSelectedWidth = BarWidth.plus(4.dp)
internal val SmallBarHeight = 15.dp
internal val SmallSelectedBarHeight = SmallBarHeight.plus(10.dp)

internal val MediumBarHeight = SmallSelectedBarHeight
internal val MediumSelectedBarHeight = MediumBarHeight.plus(10.dp)

class BarItemState(
    height: Dp,
    width: Dp,
    color: Color
) {
    val animatedHeight = Animatable(height.value)
    val animatedColor = androidx.compose.animation.Animatable(color)
    val animatedWidth = Animatable(width.value)

    val height: Float
        get() = animatedHeight.value
    val width: Float
        get() = animatedWidth.value
    val color: Color
        get() = animatedColor.value

    suspend fun animateColorChange(color: Color) {
        if (animatedColor.targetValue == color) return
        animatedColor.animateTo(color, tween(700))
    }

    suspend fun animateHeightChange(height: Dp) {
        if (animatedHeight.targetValue == height.value) return
        animatedHeight.animateTo(height.value, tween(700))
    }

    suspend fun animateWidthChange(width: Dp) {
        if (animatedWidth.targetValue == width.value) return
        animatedWidth.animateTo(width.value, tween(700))
    }
}

@Composable
fun BarItem(
    segmentWidth: Dp,
    alpha: Float,
    offsetX: Float,
    numSegments: Int,
    i: Int,
    isSelected: Boolean,
    barColor: Color,
    barStatesMap: MutableMap<Int, BarItemState>,
) {
    val state = barStatesMap.getOrPut(i) {
        BarItemState(
            height = when {
                isSelected && i % numSegments == 0 -> MediumSelectedBarHeight
                isSelected -> SmallSelectedBarHeight
                i % numSegments == 0 -> MediumBarHeight
                else -> SmallBarHeight
            },
            color = if (isSelected) barColor else barColor.copy(0.2f),
            width = if (isSelected) BarSelectedWidth else BarWidth
        )
    }

    // I'm sorry
    rememberCoroutineScope().launch {
        launch {
            val targetHeight = when {
                isSelected && i % numSegments == 0 -> MediumSelectedBarHeight
                isSelected -> SmallSelectedBarHeight
                i % numSegments == 0 -> MediumBarHeight
                else -> SmallBarHeight
            }
            if (state.animatedHeight.targetValue == targetHeight.value) return@launch
            state.animateHeightChange(targetHeight)
            if (targetHeight == MediumBarHeight) barStatesMap.remove(i)
            if (targetHeight == SmallBarHeight) barStatesMap.remove(i)
        }
        launch {
            val targetColor = if (isSelected) barColor else barColor.copy(0.2f)
            if (state.animatedColor.targetValue == targetColor) return@launch
            state.animateColorChange(targetColor)
            if (targetColor == barColor.copy(0.2f)) barStatesMap.remove(i)
        }
        launch {
            val targetWidth = if (isSelected) BarSelectedWidth else BarWidth
            if (state.animatedWidth.targetValue == targetWidth.value) return@launch
            state.animateWidthChange(targetWidth)
        }
    }

    Column(
        modifier = Modifier
            .padding(top = with(LocalDensity.current) { 54.sp.toDp() })
            .width(segmentWidth)
            .height(MediumSelectedBarHeight)
            .graphicsLayer(
                alpha = alpha,
                translationX = offsetX
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(state.width.dp)
                .height(state.height.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(state.color)
        )

    }
}

@Composable
fun PodcastSlider(
    modifier: Modifier = Modifier,
    state: PodcastSliderState = rememberPodcastSliderState(),
    density: Int,
    numSegments: Int,
    minAlpha: Float = .25f,
    barColor: Color = LocalPallet.current.white.invert,
    indicatorLabel: @Composable (Float, Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .drag(state = state, numSegments = numSegments, density = density),
            contentAlignment = Alignment.TopCenter,
        ) {
            val segmentWidth = maxWidth / density

            val segmentWidthPx = constraints.maxWidth.toFloat() / density.toFloat()
            val halfSegments = (density + 1) / 2
            val start = (state.currentValue - halfSegments).toInt()
                .coerceAtLeast(state.range.start)
            val end = (state.currentValue + halfSegments).toInt()
                .coerceAtMost(state.range.endInclusive)

            val maxOffset = constraints.maxWidth / 2f
            val barStatesMap = remember { HashMap<Int, BarItemState>() }
            for (i in start..end) {
                val offsetX = (i - state.currentValue) * segmentWidthPx
                // indicator at center is at 1f, indicators at edges are at 0.25f
                val alpha = when {
                    minAlpha == 1f -> 1f
                    else -> 1f - (1f - minAlpha) * (offsetX / maxOffset).absoluteValue
                }
                val isSelected = state.selected.value == i
                Box(
                    modifier = Modifier
                        .graphicsLayer(
                            alpha = alpha,
                            translationX = offsetX
                        ),
                    contentAlignment = Alignment.Center,
                    content = {
                        indicatorLabel(alpha, i)
                    }
                )
                BarItem(
                    segmentWidth = segmentWidth,
                    alpha = alpha,
                    offsetX = offsetX,
                    numSegments = numSegments,
                    i = i,
                    isSelected = isSelected,
                    barColor = barColor,
                    barStatesMap = barStatesMap,
                )
            }
        }
    }
}