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
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.TimerAppBarComposable
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes

@Composable
@Suppress("LongMethod")
fun TimerActiveComposableScreen(
    timerState: ControlledTimerState,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    workPhaseText: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(color = 0xFF191919)) // todo
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(color = 0xFFEF2828).copy(alpha = 0.1f), // todo
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                )
        )
        TimerAppBarComposable(
            statusType = StatusType.BUSY,
            modifier = Modifier.align(Alignment.TopCenter),
            workPhaseText = workPhaseText
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerTimeComposable(
                timerState = timerState.timerState
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val contentPadding = PaddingValues(
                    vertical = 24.dp
                )
                ButtonTimerComposable(
                    state = when {
                        timerState.isOnPause -> ButtonTimerState.START
                        else -> ButtonTimerState.PAUSE
                    },
                    onClick = onPauseClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = contentPadding
                )
                ButtonTimerComposable(
                    state = ButtonTimerState.STOP,
                    onClick = onStopClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = contentPadding
                )
            }
            onSkipClick?.let {
                BChipButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Skip",
                    painter = null,
                    background = Color.Transparent,
                    onClick = onSkipClick,
                    contentColor = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                        .copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
@Preview
private fun MainScreenComposableScreenPreview() {
    BusyBarThemeInternal {
        TimerActiveComposableScreen(
            modifier = Modifier.fillMaxSize(),
            timerState = ControlledTimerState(
                timerState = TimerState(32.minutes),
                isOnPause = false
            ),
            workPhaseText = "1/4\nwork phase",
            onPauseClick = {},
            onSkipClick = {},
            onStopClick = {},
        )
    }
}
