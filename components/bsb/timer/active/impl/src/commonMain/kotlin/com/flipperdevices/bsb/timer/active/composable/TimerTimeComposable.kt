package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.data.timer.TimerState

@Composable
fun TimerTimeComposable(
    timerState: TimerState,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TimerNumberComposable(timerState.minute)
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = ":", // todo raw string
            fontSize = 64.sp,
            color = LocalPallet.current.white.invert,
            fontWeight = FontWeight.W500,
            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
            textAlign = TextAlign.Center
        )
        TimerNumberComposable(timerState.second)
    }
}

private const val MIN_TWO_DIGIT_VALUE = 10

@Composable
private fun TimerNumberComposable(number: Int) {
    val numberText = if (number < MIN_TWO_DIGIT_VALUE) {
        "0$number"
    } else {
        number.toString()
    }
    Row {
        numberText.forEach { symbol ->
            AnimatedContent(
                targetState = symbol,
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 2.dp),
                    text = symbol.toString(),
                    fontSize = 64.sp,
                    color = LocalPallet.current.white.invert,
                    fontWeight = FontWeight.W500,
                    fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
