package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.active.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.active.impl.generated.resources.ta_skip
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusLowBarComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.TimerAppBarComposable
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.timeline.util.toFormattedTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
@Suppress("LongMethod")
fun TimerOnComposableScreen(
    timeLeft: Duration,
    onPauseClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    workPhaseText: String? = null,
    onSkip: (() -> Unit)? = null,
) {
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
    Box(modifier = modifier.fillMaxSize().navigationBarsPadding().statusBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerAppBarComposable(onBackClick = onBack)
            StatusLowBarComposable(
                type = StatusType.BUSY,
                statusDesc = workPhaseText
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                timeLeft.toComponents { days, hours, minutes, seconds, nanoseconds ->
                    val timeComponentList = listOfNotNull(
                        hours.takeIf { h -> h > 0 },
                        minutes,
                        seconds
                    )
                    Text(
                        text = timeComponentList.joinToString(
                            separator = ":",
                            prefix = "",
                            transform = { timeComponent -> timeComponent.toFormattedTime() }
                        ),
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalCorruptedPallet.current.white.invert
                        )
                    )
                }
            }
            onSkip?.let {
                BChipButton(
                    onClick = onSkip,
                    background = Color.Transparent,
                    text = stringResource(Res.string.ta_skip),
                    painter = null,
                    fontSize = 17.sp,
                    contentColor = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .secondary,
                    modifier = Modifier.fillMaxWidth(fraction = 0.4f)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonTimerComposable(
                modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                state = ButtonTimerState.PAUSE,
                onClick = onPauseClick
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview
private fun MainScreenComposableScreenPreview() {
    BusyBarThemeInternal {
        TimerOnComposableScreen(
            modifier = Modifier.fillMaxSize(),
            workPhaseText = "1/4",
            timeLeft = 13.minutes.plus(10.seconds),
            onSkip = {},
            onPauseClick = {},
            onBack = {}
        )
    }
}
