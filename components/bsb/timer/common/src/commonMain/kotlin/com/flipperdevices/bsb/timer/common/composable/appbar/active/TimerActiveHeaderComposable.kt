package com.flipperdevices.bsb.timer.common.composable.appbar.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_skip
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.components.bsb.timer.common.generated.resources.ta_skip
import busystatusbar.components.bsb.timer.common.generated.resources.ta_stop
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.currentUiIteration
import com.flipperdevices.bsb.timer.background.model.maxUiIterations
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusLowBarComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimerActiveHeaderComposable(
    state: ControlledTimerState.InProgress.Running,
    statusType: StatusType,
    onBack: () -> Unit,
    onSkip: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val workPhaseText = when {
        !state.timerSettings.intervalsSettings.isEnabled -> null
        else -> {
            "${state.currentUiIteration}/${state.maxUiIterations}"
        }
    }
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SmallControlChipButton(
                text = Res.string.ta_stop,
                icon = Res.drawable.ic_stop,
                onClick = onBack,
            )

            if (onSkip != null) {
                SmallControlChipButton(
                    text = Res.string.ta_skip,
                    icon = Res.drawable.ic_skip,
                    onClick = onSkip,
                )
            }
        }

        StatusLowBarComposable(
            modifier = Modifier.padding(top = 32.dp),
            type = statusType,
            statusDesc = workPhaseText
        )
    }
}

@Composable
private fun SmallControlChipButton(
    text: StringResource,
    icon: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BChipButton(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp),
        text = stringResource(text),
        painter = painterResource(icon),
        fontSize = 18.sp,
        iconSize = 12.dp,
        contentColor = LocalCorruptedPallet.current.transparent.whiteInvert.secondary,
        background = LocalCorruptedPallet.current.transparent.whiteInvert.quaternary,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        onClick = onClick,
        spacedBy = 8.dp
    )
}
