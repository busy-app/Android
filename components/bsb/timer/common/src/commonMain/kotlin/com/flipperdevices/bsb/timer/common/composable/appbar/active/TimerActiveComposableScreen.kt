package com.flipperdevices.bsb.timer.common.composable.appbar.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState

@Composable
@Suppress("LongMethod")
fun TimerActiveComposableScreen(
    state: ControlledTimerState.InProgress.Running,
    config: TimerActiveConfiguration,
    onPauseClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSkip: (() -> Unit)? = null,
) = Box(modifier = modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        config.gradientStartColor,
                        config.gradientEndColor,
                    )
                )
            )
    )
    Box(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        TimerActiveHeaderComposable(
            state = state,
            onBack = onBack,
            onSkip = onSkip,
            statusType = config.statusType
        )

        TimerCardComposable(
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 24.dp),
            timerState = state,
            config = config,
        )

        ButtonTimerComposable(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 16.dp)
                .fillMaxWidth(fraction = 0.6f),
            state = ButtonTimerState.PAUSE,
            onClick = onPauseClick
        )
    }
}
