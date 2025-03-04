package com.flipperdevices.bsbwearable.autopause.composable

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_rest_action
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_rest_desc
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_rest_title
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_work_action
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_work_desc
import busystatusbar.instances.bsb_wear.generated.resources.bwau_after_work_title
import busystatusbar.instances.bsb_wear.generated.resources.ic_checkmark
import busystatusbar.instances.bsb_wear.generated.resources.ic_laptop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.button.BIconButton
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CTRes

@Suppress("LongMethod")
@Composable
private fun AutoPauseScreenComposable(
    painter: Painter,
    title: String,
    desc: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(color = 0xFF000000),
                        Color(color = 0xFF0E1448),
                    )
                )
            )
    )

    Column(
        modifier = modifier
            .padding(vertical = 14.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painter,
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp),
                contentDescription = null
            )
            Text(
                text = title,
                fontSize = 20.sp,
                color = LocalCorruptedPallet.current.white.onColor,
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = Color(color = 0x80FFFFFF) // todo
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BIconButton(
                painter = painterResource(CTRes.drawable.ic_stop),
                background = Color(color = 0x1AFFFFFF), // todo
                shape = CircleShape,
                onClick = onStopClick,
                modifier = Modifier.size(34.dp)
            )
            BChipButton(
                modifier = Modifier,
                onClick = onButtonClick,
                text = buttonText,
                painter = null,
                contentColor = LocalCorruptedPallet.current.black.onColor,
                background = LocalCorruptedPallet.current.white.onColor,
                fontSize = 16.sp,
                contentPadding = PaddingValues(
                    vertical = 10.dp,
                    horizontal = 14.dp
                )
            )
        }
    }
}

@Composable
internal fun AutoPauseScreenComposable(
    state: ControlledTimerState.InProgress.Await,
    onButtonClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AutoPauseScreenComposable(
        modifier = modifier,
        onStopClick = onStopClick,
        onButtonClick = onButtonClick,
        painter = painterResource(
            resource = when (state.type) {
                ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> Res.drawable.ic_checkmark
                ControlledTimerState.InProgress.AwaitType.AFTER_REST -> Res.drawable.ic_laptop
            }
        ),
        title = when (state.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> stringResource(
                Res.string.bwau_after_work_title,
                state.timerSettings.name,
                "${state.currentIteration}",
                "${state.maxIterations}"
            )

            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> stringResource(Res.string.bwau_after_rest_title)
        },
        desc = when (state.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> stringResource(Res.string.bwau_after_work_desc)
            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> stringResource(Res.string.bwau_after_rest_desc)
        },
        buttonText = when (state.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> stringResource(Res.string.bwau_after_work_action)
            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> stringResource(
                Res.string.bwau_after_rest_action,
                state.timerSettings.name
            )
        }
    )
}

@Preview
@Composable
private fun PreviewAutoPauseWorkScreenComposable() {
    BusyBarThemeInternal {
        AutoPauseScreenComposable(
            state = ControlledTimerState.InProgress.Await(
                timerSettings = TimerSettings(),
                currentIteration = 1,
                maxIterations = 3,
                pausedAt = Instant.DISTANT_PAST,
                type = ControlledTimerState.InProgress.AwaitType.AFTER_REST
            ),
            onStopClick = {},
            onButtonClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAutoPauseRestScreenComposable() {
    BusyBarThemeInternal {
        AutoPauseScreenComposable(
            state = ControlledTimerState.InProgress.Await(
                timerSettings = TimerSettings(),
                currentIteration = 1,
                maxIterations = 3,
                pausedAt = Instant.DISTANT_PAST,
                type = ControlledTimerState.InProgress.AwaitType.AFTER_WORK
            ),
            onStopClick = {},
            onButtonClick = {},
        )
    }
}
