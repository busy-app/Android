package com.flipperdevices.bsb.timer.finish.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_heart
import com.composables.core.Icon
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.finish.api.FinishTimerScreenDecomposeComponent
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// todo This method will be rewritten with another design
@Suppress("LongMethod")
@Composable
fun FinishComposableContent(
    breakType: FinishTimerScreenDecomposeComponent.BreakType,
    doneRange: IntRange,
    onRestClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(
                                color = when (breakType) {
                                    FinishTimerScreenDecomposeComponent.BreakType.SHORT -> 0xFF1A5B33
                                    FinishTimerScreenDecomposeComponent.BreakType.LONG -> 0xFF27486C
                                }
                            ), // todo
                            Color(
                                color = when (breakType) {
                                    FinishTimerScreenDecomposeComponent.BreakType.SHORT -> 0xFF0B2A1C
                                    FinishTimerScreenDecomposeComponent.BreakType.LONG -> 0xFF0E1E48
                                }
                            ), // todo
                        )
                    )
                )
        )
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_heart),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Unspecified
            )
            Text(
                text = "${doneRange.first}/${doneRange.last} done!",
                fontSize = 32.sp,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current
                    .white
                    .invert
            )
            Text(
                text = when (breakType) {
                    FinishTimerScreenDecomposeComponent.BreakType.SHORT -> {
                        "It’s time to take a break.\nRest is a key to making Pomodoro work!"
                    }

                    FinishTimerScreenDecomposeComponent.BreakType.LONG -> {
                        "You've just completed all your work phases.\nNow it's time for a longer break."
                    }
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center,
                color = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(32.dp.minus(12.dp)))
            BChipButton(
                onClick = onRestClick,
                text = "Rest",
                fontSize = 24.sp,
                painter = null,
                contentPadding = PaddingValues(
                    horizontal = 64.dp,
                    vertical = 16.dp
                ),
                background = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .tertiary
                    .copy(alpha = 0.1f),
                contentColor = LocalPallet.current
                    .white
                    .invert
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            BChipButton(
                onClick = onDoneClick,
                text = "I'm done for today",
                fontSize = 18.sp,
                painter = null,
                background = Color.Transparent,
                contentColor = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun FinishLongComposableContentPreview() {
    BusyBarThemeInternal {
        FinishComposableContent(
            modifier = Modifier.fillMaxSize(),
            breakType = FinishTimerScreenDecomposeComponent.BreakType.LONG,
            doneRange = 1..4,
            onRestClick = {},
            onDoneClick = {}
        )
    }
}

@Preview
@Composable
private fun FinishShortComposableContentPreview() {
    BusyBarThemeInternal {
        FinishComposableContent(
            modifier = Modifier.fillMaxSize(),
            breakType = FinishTimerScreenDecomposeComponent.BreakType.SHORT,
            doneRange = 4..4,
            onRestClick = {},
            onDoneClick = {}
        )
    }
}
