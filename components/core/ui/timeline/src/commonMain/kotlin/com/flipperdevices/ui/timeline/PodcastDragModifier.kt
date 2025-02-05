package com.flipperdevices.ui.timeline

import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlin.math.roundToInt
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal fun Modifier.drag(
    state: PodcastSliderState,
    numSegments: Int,
    density: Int
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)
    val segmentWidthPx = size.width / density
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
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            }

            val value = state.currentValue.roundToInt().let { roundValue ->
                val mod = roundValue % numSegments
                val addition = if (mod > numSegments.div(2)) numSegments else 0
                (roundValue / numSegments) * numSegments.toFloat() + addition
            }
            val targetValue = decay.calculateTargetValue(value, 0f)
            launch {
                state.decayTo(0f, targetValue)
            }
        }
    }
}