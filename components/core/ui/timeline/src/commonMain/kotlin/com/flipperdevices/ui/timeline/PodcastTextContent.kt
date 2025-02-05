package com.flipperdevices.ui.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.launch

/**
 * Format seconds 0 -> 00
 * Need when 9:2:3 -> 0:02:03
 */
private fun Int.formattedTime(): String {
    return if (this < 10) "0$this" else "$this"
}


@Composable
fun TextContent(
    value: Int,
    isSelected: Boolean,
    animatedTextState: AnimatedTextState
) {
    val scope = rememberCoroutineScope()
    scope.launch {
        animatedTextState.animateFontSize(
            key = value,
            targetValue = when {
                value == 0 && isSelected -> 85f
                isSelected -> 48f
                else -> 24f
            }
        )
    }
    scope.launch {
        animatedTextState.animateColor(
            key = value,
            alpha = when {
                isSelected -> 1f
                value % 10 == 0 -> 0.4f
                else -> 0f
            }
        )
    }


    val duration = value.seconds
    duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        val text = when {
            days > 0 -> "${days}d ${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            hours > 0 -> "${hours}h ${minutes.formattedTime()}m ${seconds.formattedTime()}s"
            minutes > 0 -> "${minutes}:${seconds.formattedTime()}"
            value == 0 -> "∞"
            else -> "${seconds}"
        }

        Text(
            modifier = Modifier.wrapContentHeight(
                align = Alignment.CenterVertically, // aligns to the center vertically (default value)
                unbounded = true // Makes sense if the container size less than text's height
            ),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = animatedTextState.getFontSize(value, isSelected).sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            color = LocalPallet.current
                .white
                .invert
                .copy(animatedTextState.getAlpha(value, isSelected))
        )
    }
}

class AnimatedTextState(private val defaultFontSize: Float, private val defaultAlpha: Float) {
    private val animations = mutableMapOf<Int, Animatable<Float, AnimationVector1D>>()
    private val colors = mutableMapOf<Int, Animatable<Float, AnimationVector1D>>()
    suspend fun animateFontSize(key: Int, targetValue: Float) {
        val animation = animations.getOrPut(key) {
            Animatable(defaultFontSize)
        }
        if (animation.value == targetValue) return
        animation.animateTo(
            targetValue = targetValue,
            animationSpec = tween(70)
        )
        if (targetValue == defaultFontSize) animations.remove(key)
    }

    suspend fun animateColor(key: Int, alpha: Float) {
        val animation = colors.getOrPut(key) {
            Animatable(defaultAlpha)
        }
        if (animation.value == alpha) return
        animation.animateTo(
            targetValue = alpha,
            animationSpec = tween(50)
        )
        if (alpha == defaultAlpha) colors.remove(key)
        if (alpha == 0f) colors.remove(key)
    }

    fun getFontSize(key: Int, isSelected: Boolean) = animations[key]?.value ?: when {
        key == 0 && isSelected -> 85f
        isSelected -> 48f
        else -> 24f
    }

    fun getAlpha(key: Int, isSelected: Boolean) = colors[key]?.value ?: when {
        isSelected -> 1f
        key % 10 == 0 -> 0.4f
        else -> 0f
    }
}