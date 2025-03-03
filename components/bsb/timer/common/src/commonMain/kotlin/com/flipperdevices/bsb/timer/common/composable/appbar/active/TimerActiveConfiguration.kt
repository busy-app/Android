package com.flipperdevices.bsb.timer.common.composable.appbar.active

import androidx.compose.ui.graphics.Color
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import org.jetbrains.compose.resources.DrawableResource

data class TimerActiveConfiguration(
    val gradientStartColor: Color,
    val gradientEndColor: Color,
    val statusType: StatusType,
    val videoUri: String,
    val firstFrame: DrawableResource,
    val progressBarColor: Color,
    val progressBarBackgroundColor: Color,
    val videoBackgroundColor: Color,
)
