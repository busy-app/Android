package com.flipperdevices.ui.timeline

/**
 * Calculates the transparency level for a line based on its position within the `WheelPicker`.
 *
 * This function determines the transparency level for a line in the `WheelPicker` based on
 * its index and its position relative to the visible items in the list. The transparency
 * gradually increases towards the edges of the picker, creating a fade-out effect.
 *
 * @param lineIndex The index of the current line being rendered.
 * @param totalLines The total number of lines in the picker.
 * @param bufferIndices The number of extra indices used for rendering outside the visible area.
 * @param firstVisibleItemIndex The index of the first visible item in the list.
 * @param lastVisibleItemIndex The index of the last visible item in the list.
 * @param fadeOutLinesCount The number of lines that should gradually fade out at the edges.
 * @param maxFadeTransparency The maximum transparency level to apply during the fade-out effect.
 * @return A `Float` value representing the calculated transparency level for the line.
 *
 */
@Suppress("LongParameterList")
internal fun calculateLineTransparency(
    lineIndex: Int,
    totalLines: Int,
    bufferIndices: Int,
    firstVisibleItemIndex: Int,
    lastVisibleItemIndex: Int,
    fadeOutLinesCount: Int,
    maxFadeTransparency: Float
): Float {
    val actualCount = fadeOutLinesCount + 1
    val transparencyStep = maxFadeTransparency / actualCount

    return when {
        lineIndex < bufferIndices || lineIndex > (totalLines + bufferIndices) -> 0.0f
        lineIndex in firstVisibleItemIndex until firstVisibleItemIndex + fadeOutLinesCount -> {
            transparencyStep * (lineIndex - firstVisibleItemIndex + 1)
        }

        lineIndex in (lastVisibleItemIndex - fadeOutLinesCount + 1)..lastVisibleItemIndex -> {
            transparencyStep * (lastVisibleItemIndex - lineIndex + 1)
        }

        else -> 1.0f
    }
}
