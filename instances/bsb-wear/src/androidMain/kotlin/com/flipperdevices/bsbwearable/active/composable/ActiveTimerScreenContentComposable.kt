package com.flipperdevices.bsbwearable.active.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_next
import busystatusbar.components.bsb.timer.common.generated.resources.ic_pause
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.instances.bsb_wear.generated.resources.bwt_pause
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.progress
import com.flipperdevices.ui.autosizetext.AutoSizeText
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.button.BIconButton
import com.flipperdevices.ui.timeline.util.toFormattedTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@Suppress("LongMethod")
fun ActiveTimerScreenContentComposable(
    timerState: ControlledTimerState.InProgress.Running,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(vertical = 14.dp)
            .padding(top = 14.dp)
    ) {
        val (header, timerLine, button) = createRefs()

        Box(
            modifier = Modifier.constrainAs(header) {
                height = Dimension.matchParent
                top.linkTo(parent.top)
                bottom.linkTo(timerLine.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            contentAlignment = Alignment.TopCenter
        ) {
            ActiveTimerScreenTitleComposable(
                timerState = timerState,
                backgroundColor = timerState.getStatusColor()
            )
        }

        TimerLineComposable(
            modifier = Modifier
                .constrainAs(timerLine) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onStopClick = onStopClick,
            onSkipClick = onSkipClick,
            timerState = timerState
        )

        Box(
            modifier = Modifier.constrainAs(button) {
                height = Dimension.matchParent
                top.linkTo(timerLine.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            contentAlignment = Alignment.BottomCenter
        ) {
            BChipButton(
                onClick = onPauseClick,
                text = stringResource(busystatusbar.instances.bsb_wear.generated.resources.Res.string.bwt_pause),
                painter = painterResource(Res.drawable.ic_pause),
                contentColor = Color(color = 0x80FFFFFF),
                background = Color(color = 0x1AFFFFFF),
                fontSize = 16.sp,
                iconSize = 12.dp,
                spacedBy = 8.dp,
                contentPadding = PaddingValues(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
            )
        }
    }
}

@Composable
private fun TimerLineComposable(
    timerState: ControlledTimerState.InProgress.Running,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BIconButton(
                painter = painterResource(Res.drawable.ic_stop),
                background = Color(color = 0x1AFFFFFF),
                shape = CircleShape,
                onClick = onStopClick,
                modifier = Modifier.size(34.dp),
            )
            AutoSizeText(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                text = timerState.rememberTimeLeftText(),
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                color = LocalCorruptedPallet.current.white.invert,
                maxLines = 1
            )
            BIconButton(
                painter = painterResource(Res.drawable.ic_next),
                background = Color(color = 0x1AFFFFFF),
                shape = CircleShape,
                onClick = onSkipClick,
                modifier = Modifier.size(34.dp),
                enabled = when (timerState.timeLeft) {
                    is TimerDuration.Finite -> true
                    TimerDuration.Infinite -> false
                }
            )
        }
        if (timerState.timerSettings.totalTime is TimerDuration.Finite) {
            LinearProgressIndicator(
                modifier = Modifier.padding(horizontal = 14.dp),
                progress = animateFloatAsState(timerState.progress).value,
                color = timerState.getStatusColor()
            )
        }
    }
}

@Composable
private fun ControlledTimerState.InProgress.Running.getStatusColor(): Color {
    return when (this) {
        is ControlledTimerState.InProgress.Running.LongRest,
        is ControlledTimerState.InProgress.Running.Rest -> Color(color = 0xFF00AC34) // todo
        is ControlledTimerState.InProgress.Running.Work ->
            LocalCorruptedPallet
                .current
                .accent
                .brand
                .primary
    }
}

@Composable
private fun ControlledTimerState.InProgress.Running.rememberTimeLeftText(): String {
    return remember(timeLeft, timePassed) {
        val time = when (val localTimeLeft = timeLeft) {
            is TimerDuration.Finite -> {
                localTimeLeft.instance
            }

            TimerDuration.Infinite -> timePassed
        }
        time.toComponents { days, hours, minutes, seconds, nanoseconds ->
            val timeComponentList = listOfNotNull(
                hours.takeIf { h -> h > 0 },
                minutes,
                seconds
            )
            timeComponentList.joinToString(
                separator = ":",
                prefix = "",
                transform = { timeComponent -> timeComponent.toFormattedTime() }
            )
        }
    }
}
