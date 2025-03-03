package com.flipperdevices.bsb.timer.finish.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import busystatusbar.components.bsb.timer.rest.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.rest.impl.generated.resources.busy_smoke_first_frame
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.active.TimerActiveComposableScreen
import com.flipperdevices.bsb.timer.common.composable.appbar.active.TimerActiveConfiguration
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TimerRestComposableScreen(
    state: ControlledTimerState.InProgress.Running,
    statusType: StatusType,
    onBackClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSkip: (() -> Unit)? = null,
) {
    val configuration = TimerActiveConfiguration(
        gradientStartColor = when (statusType) {
            StatusType.BUSY -> error("Wrong status type")
            StatusType.REST -> Color(color = 0xFF416605)
            StatusType.LONG_REST -> Color(color = 0xFF003976)
        },
        gradientEndColor = when (statusType) {
            StatusType.BUSY -> error("Wrong status type")
            StatusType.REST -> Color(color = 0xFF0D1500)
            StatusType.LONG_REST -> Color(color = 0xFF001A36)
        },
        statusType = statusType,
        videoUri = Res.getUri("files/busy_smoke.mp4"),
        firstFrame = Res.drawable.busy_smoke_first_frame,
        progressBarColor = Color(color = 0xFF00AC34),
        progressBarBackgroundColor = Color(color = 0x1A00AC34),
        videoBackgroundColor = Color(color = 0xFF213c00)
    )

    TimerActiveComposableScreen(
        state = state,
        config = configuration,
        onPauseClick = onPauseClick,
        onBack = onBackClick,
        modifier = modifier,
        onSkip = onSkip,
    )
}
