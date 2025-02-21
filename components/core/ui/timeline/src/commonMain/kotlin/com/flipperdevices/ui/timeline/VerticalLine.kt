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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.timeline.model.PickerLineStyle
import com.flipperdevices.ui.timeline.util.animateTextUnitAsState
import kotlin.math.sqrt

@Composable
internal fun calculateTlr(
    adjustedIndex: Int,
    style: PickerLineStyle,
    isSelected: Boolean,
    isVisible: Boolean,
    transform: (Int) -> String
): TextLayoutResult {
    val textMeasurer = rememberTextMeasurer()
    val fontSize by animateTextUnitAsState(
        targetValue = when {
            isSelected && adjustedIndex == 0 -> style.selectedZeroFontSize
            isSelected -> style.selectedFontSize
            else -> style.unselectedFontSize
        },
        animationSpec = tween()
    )
    val fontColor by animateColorAsState(
        targetValue = when {
            !isVisible -> Color.Transparent
            isSelected ->
                LocalPallet.current
                    .white
                    .invert

            else ->
                LocalPallet.current
                    .white
                    .invert
                    .copy(alpha = 0.2f)
        }
    )
    val textColor by animateColorAsState(
        targetValue = fontColor.copy(
            alpha = when {
                adjustedIndex % style.step.times(2) == 0 -> 1f
                (isSelected && adjustedIndex % style.step == 0) -> 1f
                else -> 0f
            }.coerceAtMost(fontColor.alpha)
        )
    )
    return textMeasurer.measure(
        text = transform.invoke(adjustedIndex),
        style = TextStyle(
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center,
            fontFamily = LocalBusyBarFonts.current.pragmatica
        ),
        overflow = TextOverflow.Clip
    )
}

/**
 * A composable function that renders a single vertical line in the `WheelPicker`.
 *
 * The `VerticalLine` component is used within the `WheelPicker` to represent each
 * selectable item as a vertical line. The line's appearance can be customized with
 * different heights, padding, rounded corners, colors, and transparency effects.
 *
 * @param indexAtCenter A boolean flag indicating if the line is at the center (selected item).
 * @param lineTransparency The transparency level applied to the line.
 *
 */
@Suppress("LongMethod", "MaxLineLength", "CyclomaticComplexMethod")
@Composable
internal fun VerticalLine(
    adjustedIndex: Int,
    lineStyle: PickerLineStyle,
    indexAtCenter: Boolean,
    lineTransparency: Float,
    isVisible: Boolean,
    transform: (Int) -> String
) {
    val lineHeight by animateDpAsState(
        targetValue = when {
            indexAtCenter -> lineStyle.selectedLineHeight
            adjustedIndex % lineStyle.step == 0 -> lineStyle.stepLineHeight
            else -> lineStyle.normalLineHeight
        },
        animationSpec = tween(durationMillis = 500)
    )

    val paddingBottom by animateDpAsState(
        targetValue = when {
            indexAtCenter -> sqrt(lineStyle.selectedLineHeight.value).dp / 2
            adjustedIndex % lineStyle.step == 0 -> -sqrt(lineStyle.stepLineHeight.value).dp * 2
            else -> -sqrt(lineStyle.normalLineHeight.value).dp
        },
        animationSpec = tween(durationMillis = 500)
    )
    val lineColor by animateColorAsState(
        targetValue = if (indexAtCenter) lineStyle.selectedLineColor else lineStyle.unselectedLineColor,
        animationSpec = tween(durationMillis = 500)
    )
    val lineWidth by animateDpAsState(
        targetValue = if (indexAtCenter) lineStyle.selectedLineWidth else lineStyle.lineWidth,
        animationSpec = tween(durationMillis = 600)
    )
    val localDensity = LocalDensity.current

    val tlr = calculateTlr(
        adjustedIndex = adjustedIndex,
        style = lineStyle,
        isSelected = indexAtCenter,
        transform = transform,
        isVisible = isVisible
    )
    val textYOffset by animateFloatAsState(
        targetValue = when {
            indexAtCenter ->
                -tlr.size.height
                    .div(other = 4)
                    .toFloat()

            else ->
                tlr.size.height
                    .times(other = 1.3)
                    .toFloat()
        }
    )
    Canvas(
        Modifier
            .width(1.dp)
            .height(
                height = with(localDensity) {
                    val doubleTextHeight = tlr.size.height.toDp().times(other = 2)
                    lineStyle.selectedLineHeight + doubleTextHeight
                }
            )
    ) {
        if (adjustedIndex % lineStyle.step == 0) {
            drawText(
                textLayoutResult = tlr,
                topLeft = Offset(
                    x = -tlr.size.width.div(other = 2).toFloat().plus(other = tlr.size.width % 2),
                    y = textYOffset
                )
            )
        }

        drawRoundRect(
            cornerRadius = with(localDensity) {
                CornerRadius(
                    lineStyle.lineRoundedCorners.toPx(),
                    lineStyle.lineRoundedCorners.toPx()
                )
            },
            topLeft = with(localDensity) {
                Offset(
                    x = -lineWidth.toPx() / 2,
                    y = lineStyle.selectedZeroFontSize.toPx().plus(paddingBottom.toPx())
                )
            },
            color = lineColor.copy(alpha = lineTransparency.coerceAtMost(lineColor.alpha)),
            size = with(localDensity) {
                Size(
                    width = lineWidth.toPx(),
                    height = lineHeight.toPx()
                )
            },
        )
    }
}
