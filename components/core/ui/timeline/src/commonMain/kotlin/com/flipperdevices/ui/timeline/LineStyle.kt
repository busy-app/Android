package com.flipperdevices.ui.timeline

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalPallet

@Suppress("MaxLineLength")
/**
 * @param lineWidth The width of each vertical line in the picker. Default is `2.dp`.
 * @param selectedLineHeight The height of the selected item (line) in the picker. Default is `64.dp`.
 * @param stepLineHeight The height of lines at indices that are multiples of 5. Default is `40.dp`.
 * @param normalLineHeight The height of lines that are not the selected item or multiples of 5. Default is `30.dp`.
 * @param lineSpacing The spacing between each line in the picker. Default is `8.dp`.
 * @param lineRoundedCorners The corner radius of each vertical line, applied to create rounded corners. Default is `2.dp`.
 * @param selectedLineColor The color of the selected item (line) in the picker. Default is `Color(0xFF00D1FF)`.
 * @param unselectedLineColor The color of the unselected items (lines) in the picker. Default is `Color.LightGray`.
 * @param fadeOutLinesCount The number of lines at the edges of the picker that will gradually fade out. Default is `4`.
 * @param maxFadeTransparency The maximum transparency level applied to the fading lines. Default is `0.7f`.
 */
data class LineStyle(
    // Width
    val lineWidth: Dp = 1.dp,
    val selectedLineWidth: Dp = 2.dp,
    // Height
    val selectedLineHeight: Dp = 54.dp,
    val stepLineHeight: Dp = 34.dp,
    val normalLineHeight: Dp = 16.dp,
    // Spacing
    val lineSpacing: Dp = 12.dp,
    val lineRoundedCorners: Dp = 0.dp,
    val selectedZeroFontSize: TextUnit = 50.sp,
    val selectedFontSize: TextUnit = 40.sp,
    val unselectedFontSize: TextUnit = 15.sp,
    // Color
    val selectedLineColor: Color,
    val unselectedLineColor: Color,
    // Fade
    val fadeOutLinesCount: Int = 0,
    val maxFadeTransparency: Float = 0.8f,
) {
    companion object {
        val Default: LineStyle
            @Composable
            get() = LineStyle(
                selectedLineColor = LocalPallet.current.white.invert,
                unselectedLineColor = LocalPallet.current.white.invert.copy(0.2f)
            )
    }
}
