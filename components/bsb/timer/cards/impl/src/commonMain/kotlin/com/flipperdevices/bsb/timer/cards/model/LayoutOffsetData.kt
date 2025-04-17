package com.flipperdevices.bsb.timer.cards.model

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.ArcAnimationSpec
import androidx.compose.animation.core.ArcMode.Companion.ArcAbove
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal val LocalVideoLayoutInfo = staticCompositionLocalOf {
    VideoLayoutInfo()
}

internal class VideoLayoutInfo {
    private val videoTopOffsetsDp = mutableMapOf<String, Dp>()
    private val videoHeightDpState = mutableStateOf(0.dp)

    //    private val videoTopOffsetDpState = mutableStateOf(0.dp)
    private val videoTopOffsetAnimated = Animatable(initialValue = 0f)

    val videoHeightDp: Dp
        get() = videoHeightDpState.value
    val videoTopOffsetDp: Dp
        get() = videoTopOffsetAnimated.value.dp

    @OptIn(ExperimentalAnimationSpecApi::class)
    private suspend fun recalculateVideoTopOffsetDp() {
        val dp = videoTopOffsetsDp.values
            .map(Dp::value)
            .sum()
            .dp
            .coerceAtLeast(0.dp)
        if (videoTopOffsetAnimated.targetValue == dp.value) return
        if (videoTopOffsetAnimated.isRunning) videoTopOffsetAnimated.stop()
        videoTopOffsetAnimated.animateTo(dp.value, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow))
        return
    }

    suspend fun setVideoTopOffset(vararg keyToOffset: Pair<String, Dp>) {
        videoTopOffsetsDp.clear()
        videoTopOffsetsDp.putAll(keyToOffset)
        recalculateVideoTopOffsetDp()
    }

    fun setVideoHeightDp(value: Dp) {
        videoHeightDpState.value = value
    }

    fun clearVideoHeightDp() {
        videoHeightDpState.value = 0.dp
    }
}
