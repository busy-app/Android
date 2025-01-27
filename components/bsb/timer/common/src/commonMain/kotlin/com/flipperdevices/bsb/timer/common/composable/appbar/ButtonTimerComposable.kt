package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_pause
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_pause
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_start
import busystatusbar.components.bsb.timer.common.generated.resources.timer_button_stop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

enum class ButtonTimerState {
    STARTED, STOPPED, PAUSED
}

@Composable
fun ButtonTimerComposable(state: ButtonTimerState, modifier: Modifier = Modifier) {
    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            ButtonTimerState.STARTED -> Color(0xFF2DAF18).copy(alpha = 0.1f) // todo
            ButtonTimerState.STOPPED -> LocalPallet.current.transparent.whiteInvert.quaternary.copy(
                alpha = 0.05f
            ) // todo
            ButtonTimerState.PAUSED -> LocalPallet.current.transparent.whiteInvert.quaternary.copy(
                alpha = 0.05f
            ) // todo
        }
    )
    val contentColor by animateColorAsState(
        targetValue = when (state) {
            ButtonTimerState.STARTED -> Color(0xFF2DAF18) // todo
            ButtonTimerState.STOPPED -> LocalPallet.current.transparent.whiteInvert.primary
            ButtonTimerState.PAUSED -> LocalPallet.current.transparent.whiteInvert.primary.copy(alpha = 0.5f) // todo
        }
    )

    Button(
        modifier = modifier.animateContentSize(),
        contentPadding = PaddingValues(
            horizontal = 46.dp,
            vertical = 24.dp
        ),
        shape = RoundedCornerShape(112.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        onClick = {
        },
        content = {
            Row(
                modifier = Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = {
                    val painter: Painter = painterResource(
                        resource = when (state) {
                            ButtonTimerState.STARTED -> CommonRes.drawable.ic_play
                            ButtonTimerState.STOPPED -> CommonRes.drawable.ic_stop
                            ButtonTimerState.PAUSED -> CommonRes.drawable.ic_pause
                        }
                    )
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = contentColor
                    )
                    Spacer(Modifier.width(12.dp))
                    val text: String = stringResource(
                        resource = when (state) {
                            ButtonTimerState.STARTED -> Res.string.timer_button_start
                            ButtonTimerState.STOPPED -> Res.string.timer_button_stop
                            ButtonTimerState.PAUSED -> Res.string.timer_button_pause
                        }
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = text,
                        maxLines = 1,
                        style = TextStyle(
                            textAlign = TextAlign.Start,
                            lineHeight = 24.sp,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.LastLineBottom
                            ),
                            color = contentColor,
                            fontFamily = LocalBusyBarFonts.current.pragmatica,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W500,
                        )
                    )
                }
            )
        }
    )
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

                ButtonTimerComposable(ButtonTimerState.entries[i % ButtonTimerState.entries.size])
                ButtonTimerState.entries.forEach { entry ->
                    ButtonTimerComposable(entry)
                }
            }
        }
    }
}
