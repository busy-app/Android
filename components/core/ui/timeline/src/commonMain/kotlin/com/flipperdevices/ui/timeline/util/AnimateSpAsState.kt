package com.flipperdevices.ui.timeline.util

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val TextUnitToVector: TwoWayConverter<TextUnit, AnimationVector1D> = TwoWayConverter(
    convertToVector = { AnimationVector1D(it.value) },
    convertFromVector = { it.value.sp }
)

@Composable
internal fun animateTextUnitAsState(
    targetValue: TextUnit,
    animationSpec: AnimationSpec<TextUnit> = spring(visibilityThreshold = 0.1f.sp),
    label: String = "DpAnimation",
    finishedListener: ((TextUnit) -> Unit)? = null
): State<TextUnit> {
    return animateValueAsState(
        targetValue = targetValue,
        typeConverter = TextUnitToVector,
        animationSpec = animationSpec,
        label = label,
        finishedListener = finishedListener
    )
}
