package com.flipperdevices.ui.timeline

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.math.absoluteValue

internal val BarWidth = 4.dp
internal val BarHeight = 24.dp

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
                Column(
                    modifier = Modifier
                        .padding(top = with(LocalDensity.current) { 64.sp.toDp() })
                        .width(segmentWidth)
                        .height(BarHeight.plus(20.dp))
                        .graphicsLayer(
                            alpha = alpha,
                            translationX = offsetX
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(BarWidth)
                            .height(
                                BarHeight
                                    .plus(if (i % numSegments == 0) 10.dp else 0.dp)
                                    .plus(
                                        animateDpAsState(
                                            targetValue = if (isSelected) 10.dp else 0.dp,
                                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                                        ).value
                                    )
                            )
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                animateColorAsState(
                                    targetValue = if (isSelected) barColor else barColor.copy(0.2f),
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                ).value
                            )
                    )

                }
            }
        }
    }
}