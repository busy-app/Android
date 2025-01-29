package com.flipperdevices.bsb.timer.main.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.common.composable.appbar.HintBubble
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.common.composable.appbar.TimerAppBarComposable
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Suppress("LongMethod")
fun TimerOffComposableScreen(
    modifier: Modifier = Modifier,
    workPhaseText: String? = null
) {
    Box(modifier = modifier) {
        TimerAppBarComposable(
            statusType = StatusType.OFF,
            modifier = Modifier.align(Alignment.TopCenter),
            workPhaseText = workPhaseText
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            fontWeight = FontWeight.W500,
                            fontSize = 82.sp,
                            color = LocalPallet.current.white.invert
                        ),
                        block = { append("25") }
                    )
                    withStyle(
                        style = SpanStyle(
                            fontFamily = LocalBusyBarFonts.current.pragmatica,
                            fontWeight = FontWeight.W500,
                            fontSize = 36.sp,
                            color = LocalPallet.current.white.invert
                        ),
                        block = { append("min") }
                    )
                }
            )
            BChipButton(
                text = "Work >",
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
            HintBubble(
                text = "What would you like to focus on?"
            )
            Spacer(Modifier.height(60.dp.minus(8.dp * 2)))
            ButtonTimerComposable(
                state = ButtonTimerState.START,
                onClick = {}
            )
        }
    }
}

@Composable
@Preview
private fun MainScreenComposableScreenPreview() {
    BusyBarThemeInternal {
        TimerOffComposableScreen(
            modifier = Modifier.fillMaxSize(),
            workPhaseText = "1/4\nwork phase"
        )
    }
}
