package com.flipperdevices.ui.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.launch

/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
internal fun Int.formattedTime(): String {
    return if (this < 10) "0$this" else "$this"
}
internal fun Duration.formattedTime(): String {
    return this.toComponents { days, hours, minutes, seconds, nanoseconds ->
        when {
            days > 0 -> "${days}d ${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            minutes > 0 -> "${minutes}m ${seconds.formattedTime()}s"
            seconds == 0 -> "∞"
            else -> "${seconds}s"
        }
    }
}

class AnimatedTextState(fontSize: Float, alpha: Float) {
    val fontSizeAnimated = Animatable(fontSize)
    val alphaAnimated = Animatable(alpha)
    val offsetAnimated = Animatable(0f)
    suspend fun animateFontSize(targetValue: Float) {
        val animation = fontSizeAnimated
        if (animation.targetValue == targetValue && animation.isRunning) return
        animation.animateTo(
            targetValue = targetValue,
            animationSpec = tween(400)
        )
    }

    suspend fun animateOffset(targetValue: Float) {
        val animation = offsetAnimated
        if (animation.targetValue == targetValue && animation.isRunning) return
        animation.animateTo(
            targetValue = targetValue,
            animationSpec = tween(400)
        )
    }

    suspend fun animateColor(targetValue: Float) {
        val animation = alphaAnimated
        if (animation.targetValue == targetValue && animation.isRunning) return
        animation.animateTo(
            targetValue = targetValue,
            animationSpec = tween(400)
        )
    }
}

@Composable
fun TextContent(
    value: Int,
    selected: Int,
    map: MutableMap<Int, AnimatedTextState>
) {
    val getFontSize = {
        val isSelected = selected == value
        when {
            value == 0 && isSelected -> 85f
            isSelected -> 40f
            else -> 15f
        }
    }
    val getAlpha = {
        val isSelected = selected == value
        when {
            isSelected -> 1f
            value % 10 == 0 -> 0.4f
            else -> 0f
        }
    }
    val getOffset = {
        val isSelected = selected == value
        when {
            isSelected -> 24f
            else -> 0f
        }
    }
    val state = map.getOrPut(value) {
        AnimatedTextState(
            fontSize = getFontSize.invoke(),
            alpha = getAlpha.invoke()
        )
    }
    rememberCoroutineScope().launch {
        launch {
            state.animateFontSize(getFontSize.invoke())
        }
        launch {
            state.animateOffset(getOffset.invoke())
        }
        launch {
            state.animateColor(getAlpha.invoke())
        }
        launch {
            if (state.alphaAnimated.isRunning) return@launch
            if (state.fontSizeAnimated.isRunning) return@launch
            if (state.fontSizeAnimated.value == 15f) map.remove(value)
            if (state.alphaAnimated.value == 0f) map.remove(value)
            if (state.alphaAnimated.value == 0.4f) map.remove(value)
        }
    }

    val duration = value.seconds
    duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        val text = when {
            days > 0 -> "${days}d ${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            minutes > 0 -> "${minutes}m ${seconds.formattedTime()}s"
            value == 0 -> "∞"
            else -> "${seconds}s"
        }

        Text(
            modifier = Modifier.wrapContentHeight(
                align = Alignment.CenterVertically, // aligns to the center vertically (default value)
                unbounded = true // Makes sense if the container size less than text's height
            ).graphicsLayer(
                translationY = -with(LocalDensity.current) { state.offsetAnimated.value.dp.toPx()}
            ),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = state.fontSizeAnimated.value.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            color = LocalPallet.current
                .white
                .invert
                .copy(state.alphaAnimated.value)
        )
    }
}

