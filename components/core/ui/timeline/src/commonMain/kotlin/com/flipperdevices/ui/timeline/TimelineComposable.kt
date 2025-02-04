package com.flipperdevices.ui.timeline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.compose.ui.tooling.preview.Preview

private val BarWidth = 2.dp
private val BarHeight = 24.dp
private const val MinAlpha = .25f

@Stable
interface PodcastSliderState {
    val currentValue: Float
    val range: ClosedRange<Int>

    suspend fun snapTo(value: Float)
    suspend fun decayTo(velocity: Float, value: Float)
    suspend fun stop()
}

class PodcastSliderStateImpl(
    currentValue: Float,
    override val range: ClosedRange<Int>,
) : PodcastSliderState {

    private val floatRange = range.start.toFloat()..range.endInclusive.toFloat()
    private val animatable = Animatable(currentValue)
    private val decayAnimationSpec = FloatSpringSpec(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    override val currentValue: Float
        get() = animatable.value

    override suspend fun stop() {
        animatable.stop()
    }

    override suspend fun snapTo(value: Float) {
        animatable.snapTo(value.coerceIn(floatRange))
    }

    override suspend fun decayTo(velocity: Float, value: Float) {
        val target = value.roundToInt().coerceIn(range).toFloat()
        animatable.animateTo(
            targetValue = target,
            initialVelocity = velocity,
            animationSpec = decayAnimationSpec,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as PodcastSliderStateImpl

        if (range != other.range) return false
        if (floatRange != other.floatRange) return false
        if (animatable != other.animatable) return false
        if (decayAnimationSpec != other.decayAnimationSpec) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + floatRange.hashCode()
        result = 31 * result + animatable.hashCode()
        result = 31 * result + decayAnimationSpec.hashCode()
        return result
    }

    companion object {
        val Saver = Saver<PodcastSliderStateImpl, List<Any>>(
            save = { listOf(it.currentValue, it.range.start, it.range.endInclusive) },
            restore = {
                PodcastSliderStateImpl(
                    currentValue = it[0] as Float,
                    range = (it[1] as Int)..(it[2] as Int)
                )
            }
        )
    }
}

@Composable
fun rememberPodcastSliderState(
    currentValue: Float = 12f,
    range: ClosedRange<Int> = 5..30,
): PodcastSliderState {
    val state = rememberSaveable(saver = PodcastSliderStateImpl.Saver) {
        PodcastSliderStateImpl(currentValue, range)
    }
    LaunchedEffect(key1 = Unit) {
        state.snapTo(state.currentValue.roundToInt().toFloat())
    }
    return state
}

@Composable
fun PodcastSlider(
    modifier: Modifier = Modifier,
    state: PodcastSliderState = rememberPodcastSliderState(),
    density: Int,
    numSegments: Int,
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
                .drag(state, density),
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
                val alpha = 1f - (1f - MinAlpha) * (offsetX / maxOffset).absoluteValue
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
                        .padding(top = with(LocalDensity.current) { 85.sp.toDp() })
                        .width(segmentWidth)
                        .graphicsLayer(
                            alpha = alpha,
                            translationX = offsetX
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (i % numSegments != 0) {
                        Spacer(Modifier.height(10.dp.div(2)))
                    }
                    Box(
                        modifier = Modifier
                            .width(BarWidth)
                            .height(
                                BarHeight
                                    .plus(if (i % numSegments == 0) 10.dp else 0.dp)
                                    .plus(if (i % numSegments == 0) alpha.times(10.dp) else 0.dp)
                            )
                            .background(barColor.copy(alpha))
                    )
                }
            }
        }
    }
}

private fun Modifier.drag(
    state: PodcastSliderState,
    numSegments: Int,
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)
    val segmentWidthPx = size.width / numSegments
    coroutineScope {
        while (true) {
            val pointerId = awaitPointerEventScope { awaitFirstDown().id }
            state.stop()
            val tracker = VelocityTracker()
            awaitPointerEventScope {
                horizontalDrag(pointerId) { change ->
                    val horizontalDragOffset =
                        state.currentValue - change.positionChange().x / segmentWidthPx
                    launch {
                        state.snapTo(horizontalDragOffset)
                    }
                    tracker.addPosition(change.uptimeMillis, change.position)
                    change.consumePositionChange()
                }
            }
            val velocity = tracker.calculateVelocity().x / numSegments
            val targetValue = decay.calculateTargetValue(state.currentValue, -velocity)
            launch {
                state.decayTo(velocity, targetValue)
            }
        }
    }
}

/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
private fun Int.formattedTime(): String {
    return if (this < 10) "0$this" else "$this"
}

@Preview
@Composable
private fun TimelineComposablePreview() {
    BusyBarThemeInternal {
        val numSegments = 5
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.Black) {
            PodcastSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                density = 25,
                numSegments = numSegments,
                state = rememberPodcastSliderState(
                    currentValue = 0.seconds.inWholeSeconds.toFloat(),
                    range = 0..9.hours.inWholeSeconds.toInt()
                ),
                indicatorLabel = { progress, value ->
                    val duration = value.seconds
                    duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
                        val text = when {
                            days > 0 -> "${days}:${hours}:${minutes.formattedTime()}:${seconds.formattedTime()}"
                            hours > 0 -> "${hours}:${minutes.formattedTime()}:${seconds.formattedTime()}"
                            minutes > 0 -> "${minutes}:${seconds.formattedTime()}"
                            value == 0 -> "∞"
                            else -> "$seconds"
                        }
                        when {
                            value % 5 == 0 -> {
                                Box(
                                    modifier = Modifier
                                        .height(with(LocalDensity.current) { 85.sp.toDp() }),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        modifier = Modifier.wrapContentHeight(
                                            align = Alignment.CenterVertically, // aligns to the center vertically (default value)
                                            unbounded = true // Makes sense if the container size less than text's height
                                        ),
                                        text = text,
                                        textAlign = TextAlign.Center,
                                        fontSize = animateIntAsState(
                                            when {
                                                value == 0 && progress > 0.9f -> 85
                                                progress < 0.8f -> 24
                                                else -> 48
                                            }
                                        ).value.sp,
                                        color = animateColorAsState(
                                            when {
                                                value % 10 == 0 -> LocalPallet.current
                                                    .white
                                                    .invert
                                                    .copy(progress)

                                                value % 5 == 0 && progress > 0.7f -> LocalPallet.current
                                                    .white
                                                    .invert
                                                    .copy(progress)

                                                else -> Color.Transparent
                                            }
                                        ).value
                                    )
                                }
                            }
                        }
                    }
                }
            )

        }
    }
}