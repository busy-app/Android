package com.flipperdevices.bsb.timer.cards.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalVideoLayoutInfo = staticCompositionLocalOf {
    VideoLayoutInfo()
}
internal class VideoLayoutInfo {
    private val videoTopOffsetsDp = mutableMapOf<String, Dp>()
    private val videoHeightDpState = mutableStateOf(0.dp)
    private val videoTopOffsetDpState = mutableStateOf(0.dp)

    val videoHeightDp: Dp
        get() = videoHeightDpState.value
    val videoTopOffsetDp: Dp
        get() = videoTopOffsetDpState.value

    private fun recalculateVideoTopOffsetDp() {
        val dp = videoTopOffsetsDp.values
            .map(Dp::value)
            .sum()
            .dp
        videoTopOffsetDpState.value = dp
    }

    fun addVideoTopOffset(key: String, offset: Dp) {
        videoTopOffsetsDp.put(key, offset)
        recalculateVideoTopOffsetDp()
    }

    fun clearVideoTopOffsets() {
        videoTopOffsetsDp.clear()
        recalculateVideoTopOffsetDp()
    }

    fun setVideoHeightDp(value: Dp) {
        videoHeightDpState.value = value
    }

    fun clearVideoHeightDp() {
        videoHeightDpState.value = 0.dp
    }
}
