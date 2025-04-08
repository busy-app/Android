package com.flipperdevices.bsbwearable.active.composable

import BaselineText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwt_long_rest
import busystatusbar.instances.bsb_wear.generated.resources.bwt_rest
import busystatusbar.instances.bsb_wear.generated.resources.tds_iteration_progress
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.currentUiIteration
import com.flipperdevices.bsb.timer.background.model.maxUiIterations
import org.jetbrains.compose.resources.stringResource


@Composable
fun ActiveTimerScreenTitleComposable(
    timerState: ControlledTimerState.InProgress.Running,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val text = when (timerState) {
            is ControlledTimerState.InProgress.Running.LongRest -> stringResource(Res.string.bwt_long_rest)
            is ControlledTimerState.InProgress.Running.Rest -> stringResource(Res.string.bwt_rest)
            is ControlledTimerState.InProgress.Running.Work -> timerState.timerSettings.name
        }

        BaselineText(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(160.dp))
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 1.dp),
            text = text,
            fontSize = 20.sp,
            color = LocalCorruptedPallet.current.white.onColor,
            fontFamily = LocalBusyBarFonts.current.pragmatica
        )
        if (timerState is ControlledTimerState.InProgress.Running.Work) {
            Text(
                text = when (timerState.timeLeft) {
                    is TimerDuration.Finite -> {
                        stringResource(
                            Res.string.tds_iteration_progress,
                            "${timerState.currentUiIteration}",
                            "${timerState.maxUiIterations}"
                        )
                    }

                    TimerDuration.Infinite -> "${timerState.currentUiIteration}"
                },
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                color = Color(color = 0x4DFFFFFF) // todo
            )
        }
    }
}
