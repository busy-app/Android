package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_pause
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_pause
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_start
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_stop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@Composable
fun ButtonTimerComposable(
    state: ButtonTimerState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 48.dp,
        vertical = 24.dp
    )
) {
    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            ButtonTimerState.START -> Color(color = 0xFF2DAF18)
                .copy(alpha = 0.1f) // todo
            ButtonTimerState.STOP ->
                LocalPallet.current
                    .transparent
                    .whiteInvert
                    .quaternary
                    .copy(alpha = 0.05f) // todo
            ButtonTimerState.PAUSE ->
                LocalPallet.current
                    .transparent
                    .whiteInvert
                    .quaternary
                    .copy(alpha = 0.05f) // todo
        }
    )
    val contentColor by animateColorAsState(
        targetValue = when (state) {
            ButtonTimerState.START -> Color(color = 0xFF2DAF18) // todo
            ButtonTimerState.STOP ->
                LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary

            ButtonTimerState.PAUSE ->
                LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f) // todo
        }
    )
    val text: String = stringResource(
        resource = when (state) {
            ButtonTimerState.START -> Res.string.timer_button_start
            ButtonTimerState.STOP -> Res.string.timer_button_stop
            ButtonTimerState.PAUSE -> Res.string.timer_button_pause
        }
    )
    val painter: Painter = painterResource(
        resource = when (state) {
            ButtonTimerState.START -> CommonRes.drawable.ic_play
            ButtonTimerState.STOP -> CommonRes.drawable.ic_stop
            ButtonTimerState.PAUSE -> CommonRes.drawable.ic_pause
        }
    )
    BChipButton(
        text = text,
        painter = painter,
        contentColor = contentColor,
        background = backgroundColor,
        onClick = onClick,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

enum class ButtonTimerState {
    START, STOP, PAUSE
}

@Preview
@Composable
private fun ButtonTimerComposablePreview() {
    var i by remember { mutableIntStateOf(0) }
    BusyBarThemeInternal {
        Scaffold(backgroundColor = Color.Black) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Click!",
                    color = Color.Red,
                    modifier = Modifier.clickable { i++ }
                )

                ButtonTimerComposable(
                    state = ButtonTimerState.entries[i % ButtonTimerState.entries.size],
                    onClick = {}
                )
                ButtonTimerState.entries.forEach { entry ->
                    ButtonTimerComposable(state = entry, onClick = {})
                }
            }
        }
    }
}
