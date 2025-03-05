package com.flipperdevices.ui.timeline.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet

/**
 * @param lineWidth The width of each vertical line in the picker. Default is `2.sdp`.
 * @param selectedLineHeight The height of the selected item (line) in the picker. Default is `64.sdp`.
 * @param stepLineHeight The height of lines at indices that are multiples of 5. Default is `40.sdp`.
 * @param normalLineHeight The height of lines that are not the selected item or multiples of 5. Default is `30.sdp`.
 * @param lineSpacing The spacing between each line in the picker. Default is `8.sdp`.
 * @param lineRoundedCorners The corner radius of each vertical line, applied to create rounded corners. Default is `2.sdp`.
 * @param selectedLineColor The color of the selected item (line) in the picker. Default is `Color(0xFF00D1FF)`.
 * @param unselectedLineColor The color of the unselected items (lines) in the picker. Default is `Color.LightGray`.
 * @param fadeOutLinesCount The number of lines at the edges of the picker that will gradually fade out. Default is `4`.
 * @param maxFadeTransparency The maximum transparency level applied to the fading lines. Default is `0.7f`.
 */
@Suppress("MaxLineLength")
data class PickerLineStyle(
    // Width
    val lineWidth: Dp,
    val selectedLineWidth: Dp = 2.dp,
    // Height
    val selectedLineHeight: Dp,
    val stepLineHeight: Dp,
    val normalLineHeight: Dp,
    // Spacing
    val lineSpacing: Dp,
    val lineRoundedCorners: Dp,
    // Color-Line
    val selectedLineColor: Color,
    val unselectedLineColor: Color,
    // Font
    val selectedZeroFontSize: TextUnit,
    val selectedFontSize: TextUnit,
    val unselectedFontSize: TextUnit,
    val step: Int,
    // Fade
    val fadeOutLinesCount: Int,
    val maxFadeTransparency: Float,
) {
    companion object {
        val Default: PickerLineStyle
            @Composable
            get() = PickerLineStyle(
                lineWidth = 2.dp,
                selectedLineHeight = 44.dp,
                stepLineHeight = 32.dp,
                normalLineHeight = 16.dp,
                lineSpacing = 16.dp,
                lineRoundedCorners = 0.dp,
                selectedLineColor = LocalCorruptedPallet.current.accent.device.primary,
                unselectedLineColor = LocalCorruptedPallet.current.white.invert.copy(0.2f),
                fadeOutLinesCount = 12,
                maxFadeTransparency = 0.95f,
                step = 5,
                selectedZeroFontSize = 42.sp,
                selectedFontSize = 28.sp,
                unselectedFontSize = 16.sp,
            )
    }
}
