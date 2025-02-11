package com.flipperdevices.bsb.timer.main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.TimerAppBarComposable
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
@Suppress("LongMethod")
fun TimerOnComposableScreen(
    tagName: String,
    timeLeft: Duration,
    modifier: Modifier = Modifier,
    workPhaseText: String? = null,
    onSkip: (() -> Unit)? = null,
    roadmapHint: String? = null
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
    Box(modifier = modifier) {
        TimerAppBarComposable(
            statusType = StatusType.BUSY,
            modifier = Modifier.align(Alignment.TopCenter),
            workPhaseText = workPhaseText
        )

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
                    Text(
                        text = "$minutes",
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalPallet.current.white.invert
                        )
                    )
                    Text(
                        text = ":",
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalPallet.current.white.invert
                        )
                    )
                    Text(
                        text = "$seconds",
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalPallet.current.white.invert
                        )
                    )
                }
            }
            BChipButton(
                text = tagName,
                painter = null,
                contentColor = LocalPallet.current.transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f),
                background = LocalPallet.current.transparent
                    .whiteInvert
                    .quaternary
                    .copy(alpha = 0.05f),
                fontSize = 14.sp,
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                ),
                onClick = {},
            )
            roadmapHint?.let {
                Text(
                    text = roadmapHint,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = LocalPallet.current.white.invert
                    )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonTimerComposable(
                    modifier = Modifier.weight(1f),
                    state = ButtonTimerState.PAUSE,
                    onClick = {}
                )
                ButtonTimerComposable(
                    modifier = Modifier.weight(1f),
                    state = ButtonTimerState.STOP,
                    onClick = {}
                )
            }
            onSkip?.let {
                BChipButton(
                    onClick = onSkip,
                    background = Color.Transparent,
                    text = "Skip",
                    painter = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
            workPhaseText = "1/4\nwork phase",
            tagName = "work",
            roadmapHint = "Finish the roadmap for mobile app",
            timeLeft = 13.minutes.plus(10.seconds),
            onSkip = {}
        )
    }
}
