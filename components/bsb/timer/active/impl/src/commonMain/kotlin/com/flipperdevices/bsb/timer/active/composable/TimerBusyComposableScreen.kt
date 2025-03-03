package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import busystatusbar.components.bsb.timer.active.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.active.impl.generated.resources.busy_fire_first_frame
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.active.TimerActiveComposableScreen
import com.flipperdevices.bsb.timer.common.composable.appbar.active.TimerActiveConfiguration
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
@OptIn(ExperimentalResourceApi::class)
fun TimerBusyComposableScreen(
    state: ControlledTimerState.InProgress.Running,
    onPauseClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSkip: (() -> Unit)? = null,
) {
    val configuration = TimerActiveConfiguration(
        gradientStartColor = Color(color = 0xFF900606),
        gradientEndColor = Color(color = 0xFF430303),
        statusType = StatusType.BUSY,
        videoUri = Res.getUri("files/busy_fire.mp4"),
        firstFrame = Res.drawable.busy_fire_first_frame,
        progressBarColor = LocalCorruptedPallet.current.accent.brand.primary,
        progressBarBackgroundColor = LocalCorruptedPallet.current.accent.brand.tertiary,
        videoBackgroundColor = Color(color = 0xFF5f170a)
    )

    TimerActiveComposableScreen(
        state = state,
        config = configuration,
        onPauseClick = onPauseClick,
        onBack = onBack,
        modifier = modifier,
        onSkip = onSkip,
    )
}
