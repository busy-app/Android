package com.flipperdevices.ui.timeline.model

data class VisibleLinesState(
    val firstVisibleItemIndex: Int,
    val lastVisibleItemIndex: Int,
) {
    val totalVisibleItems = lastVisibleItemIndex - firstVisibleItemIndex + 1
    val middleIndex = firstVisibleItemIndex + totalVisibleItems / 2
    val bufferIndices = totalVisibleItems / 2
}
