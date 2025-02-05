package com.flipperdevices.ui.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlin.math.roundToInt

@Stable
interface PodcastSliderState {
    val currentValue: Float
    val range: ClosedRange<Int>
    val selected: State<Int>

    suspend fun snapTo(value: Float)
    suspend fun decayTo(velocity: Float, value: Float)
    suspend fun stop()
}

private class PodcastSliderStateImpl(
    currentValue: Float,
    override val range: ClosedRange<Int>,
) : PodcastSliderState {
    override val selected = mutableIntStateOf(currentValue.toInt())
    private val floatRange = range.start.toFloat()..range.endInclusive.toFloat()
    private val animatable: Animatable<Float, AnimationVector1D> = Animatable(currentValue)
    private val decayAnimationSpec = FloatSpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh,
    )

    override val currentValue: Float
        get() = animatable.value

    override suspend fun stop() {
        animatable.stop()
    }

    override suspend fun snapTo(value: Float) {
        animatable.snapTo(value.coerceIn(floatRange))
        selected.value = value.toInt()
    }

    override suspend fun decayTo(velocity: Float, value: Float) {
        val target = value.roundToInt().coerceIn(range).toFloat()
        selected.value = target.toInt()
        animatable.animateTo(
            targetValue = target,
            initialVelocity = velocity,
            animationSpec = decayAnimationSpec,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as PodcastSliderStateImpl

        if (range != other.range) return false
        if (floatRange != other.floatRange) return false
        if (animatable != other.animatable) return false
        if (decayAnimationSpec != other.decayAnimationSpec) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + floatRange.hashCode()
        result = 31 * result + animatable.hashCode()
        result = 31 * result + decayAnimationSpec.hashCode()
        return result
    }

    companion object {
        val Saver = Saver<PodcastSliderStateImpl, List<Any>>(
            save = { listOf(it.currentValue, it.range.start, it.range.endInclusive) },
            restore = {
                PodcastSliderStateImpl(
                    currentValue = it[0] as Float,
                    range = (it[1] as Int)..(it[2] as Int)
                )
            }
        )
    }
}

@Composable
fun rememberPodcastSliderState(
    currentValue: Float = 12f,
    range: ClosedRange<Int> = 5..30,
): PodcastSliderState {
    val state = rememberSaveable(saver = PodcastSliderStateImpl.Saver) {
        PodcastSliderStateImpl(currentValue, range)
    }
    LaunchedEffect(key1 = Unit) {
        state.snapTo(state.currentValue.roundToInt().toFloat())
    }
    return state
}