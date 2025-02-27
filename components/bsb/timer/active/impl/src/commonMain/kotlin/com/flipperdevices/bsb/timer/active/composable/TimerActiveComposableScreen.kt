package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.active.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.active.impl.generated.resources.ic_skip
import busystatusbar.components.bsb.timer.active.impl.generated.resources.ic_stop
import busystatusbar.components.bsb.timer.active.impl.generated.resources.ta_skip
import busystatusbar.components.bsb.timer.active.impl.generated.resources.ta_stop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.currentUiIteration
import com.flipperdevices.bsb.timer.background.model.maxUiIterations
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusLowBarComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.TimerAppBarComposable
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
@Suppress("LongMethod")
fun TimerActiveComposableScreen(
    state: ControlledTimerState.Running,
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
                        Color(color = 0xFF900606), // todo
                        Color(color = 0xFF430303),
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
        )

        TimerCardComposable(
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 24.dp),
            timerState = state
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

@Composable
@Preview
private fun MainScreenComposableScreenPreview() {
    BusyBarThemeInternal {
        TimerActiveComposableScreen(
            modifier = Modifier.fillMaxSize(),
            onSkip = {},
            onPauseClick = {},
            onBack = {},
            state = ControlledTimerState.Running.Work(
                timeLeft = 13.minutes.plus(10.seconds),
                isOnPause = false,
                currentIteration = 1,
                maxIterations = 4,
                timerSettings = TimerSettings()
            )
        )
    }
}
