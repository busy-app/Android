package com.flipperdevices.ui.timeline

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

/**
 * A composable function that renders a single vertical line in the `WheelPicker`.
 *
 * The `VerticalLine` component is used within the `WheelPicker` to represent each
 * selectable item as a vertical line. The line's appearance can be customized with
 * different heights, padding, rounded corners, colors, and transparency effects.
 *
 * @param isSelected A boolean flag indicating if the line is at the center (selected item).
 * @param lineTransparency The transparency level applied to the line.
 *
 */
@Composable
@Suppress("MagicNumber", "CyclomaticComplexMethod", "LongMethod")
internal fun VerticalLine(
    index: Int,
    isSelected: Boolean,
    lineTransparency: Float,
    style: LineStyle,
    progression: IntProgression
) {
    val paddingBottom by animateDpAsState(
        targetValue = when {
            isSelected -> sqrt(style.selectedLineHeight.value).dp
            index % progression.step == 0 -> -style.stepLineHeight.value.dp / 4
            else -> -sqrt(style.normalLineHeight.value).dp
        },
        animationSpec = tween(400)
    )
    val lineHeight by animateDpAsState(
        when {
            isSelected -> style.selectedLineHeight
            index % progression.step == 0 -> style.stepLineHeight
            else -> style.normalLineHeight
        },
        tween(600)
    )
    val textMeasurer = rememberTextMeasurer()
    val fontSizeFloat by animateFloatAsState(
        targetValue = when {
            isSelected && index == 0 -> 50f
            isSelected -> 40f
            else -> 15f
        },
        animationSpec = tween()
    )
    val fontColor by animateColorAsState(
        targetValue = when {
            isSelected ->
                LocalPallet.current
                    .white
                    .invert

            else ->
                LocalPallet.current
                    .white
                    .invert
                    .copy(0.2f)
        }
    )
    val textColor by animateColorAsState(
        fontColor.copy(
            alpha = when {
                index % progression.step.times(2) == 0 -> 1f
                (isSelected && index % progression.step == 0) -> 1f
                else -> 0f
            }.coerceAtMost(fontColor.alpha)
        )
    )
    val result = remember(isSelected, textColor, fontSizeFloat, fontColor, index) {
        textMeasurer.measure(
            text = index.seconds.toFormattedTime(),
            style = TextStyle(
                fontSize = fontSizeFloat.sp,
                color = textColor,
                textAlign = TextAlign.Center
            ),
            overflow = TextOverflow.Clip
        )
    }

    val textYOffset by animateFloatAsState(
        if (isSelected) {
            -result.size.height.div(2).toFloat()
        } else {
            result.size.height.div(2).toFloat()
        }
    )
    val color by animateColorAsState(
        if (isSelected) style.selectedLineColor else style.unselectedLineColor,
        tween(600)
    )
    val width by animateDpAsState(
        if (isSelected) style.selectedLineWidth else style.lineWidth,
        tween(600)
    )
    val localDensity = LocalDensity.current

    Canvas(
        Modifier
            .width(1.dp)
            .height(style.selectedLineHeight.plus(with(localDensity) { result.size.height.toDp().times(2) }))
    ) {
        if (index % progression.step == 0) {
            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = -result.size.width.div(2).toFloat().plus(result.size.width % 2),
                    y = textYOffset
                )
            )
        }

        drawRoundRect(
            cornerRadius = CornerRadius(
                with(localDensity) { style.lineRoundedCorners.toPx() },
                with(localDensity) { style.lineRoundedCorners.toPx() }
            ),
            topLeft = Offset(
                x = -with(localDensity) { width.toPx() } / 2,
                y = with(localDensity) { 50.sp.toPx() }
                    .plus(with(localDensity) { paddingBottom.toPx() })
            ),
            color = color.copy(alpha = lineTransparency.coerceAtMost(color.alpha)),
            size = Size(
                width = with(localDensity) { width.toPx() },
                height = with(localDensity) { lineHeight.toPx() }
            ),
        )
    }
}
