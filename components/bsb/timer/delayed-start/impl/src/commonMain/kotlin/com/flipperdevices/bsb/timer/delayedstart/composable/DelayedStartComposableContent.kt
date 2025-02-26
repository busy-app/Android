package com.flipperdevices.bsb.timer.delayedstart.composable

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.ic_checkmark
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.ic_laptop
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_action_busy_done
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_action_rest_done
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_desc_busy_done
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_desc_rest_done
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_finish
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_title_busy_done
import busystatusbar.components.bsb.timer.delayed_start.impl.generated.resources.tds_title_rest_done
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.delayedstart.api.DelayedStartScreenDecomposeComponent
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("LongMethod")
@Composable
fun DelayedStartComposableContent(
    onFinishClick: () -> Unit,
    onStartClick: () -> Unit,
    timerSettings: TimerSettings,
    currentIteration: Int,
    maxIteration: Int,
    typeEndDelay: DelayedStartScreenDecomposeComponent.TypeEndDelay,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(Color(color = 0xFF212121)) // todo no color in design pallet
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(132.dp),
                tint = Color.Unspecified,
                painter = painterResource(
                    when (typeEndDelay) {
                        DelayedStartScreenDecomposeComponent.TypeEndDelay.WORK -> {
                            Res.drawable.ic_checkmark
                        }

                        DelayedStartScreenDecomposeComponent.TypeEndDelay.REST -> {
                            Res.drawable.ic_laptop
                        }
                    }
                ),
                contentDescription = null
            )
            Text(
                text = when (typeEndDelay) {
                    DelayedStartScreenDecomposeComponent.TypeEndDelay.WORK -> {
                        stringResource(
                            Res.string.tds_title_busy_done,
                            "${timerSettings.name}",
                            "$currentIteration/$maxIteration"
                        )
                    }

                    DelayedStartScreenDecomposeComponent.TypeEndDelay.REST -> {
                        stringResource(Res.string.tds_title_rest_done)
                    }
                },
                fontWeight = FontWeight.W500,
                color = LocalCorruptedPallet.current
                    .white
                    .invert,
                fontSize = 40.sp
            )
            Text(
                text = when (typeEndDelay) {
                    DelayedStartScreenDecomposeComponent.TypeEndDelay.WORK -> {
                        stringResource(Res.string.tds_desc_busy_done)
                    }

                    DelayedStartScreenDecomposeComponent.TypeEndDelay.REST -> {
                        stringResource(Res.string.tds_desc_rest_done)
                    }
                },
                fontWeight = FontWeight.W500,
                color = LocalCorruptedPallet.current
                    .white
                    .invert,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(20.dp))
            BChipButton(
                onClick = onStartClick,
                text = when (typeEndDelay) {
                    DelayedStartScreenDecomposeComponent.TypeEndDelay.WORK -> {
                        stringResource(Res.string.tds_action_busy_done)
                    }

                    DelayedStartScreenDecomposeComponent.TypeEndDelay.REST -> {
                        stringResource(
                            Res.string.tds_action_rest_done,
                            timerSettings.name
                        )
                    }
                },
                fontSize = 18.sp,
                painter = null,
                background = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .tertiary,
                contentPadding = PaddingValues(
                    horizontal = 64.dp,
                    vertical = 16.dp
                ),
                contentColor = LocalCorruptedPallet.current
                    .white
                    .invert
            )
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            BChipButton(
                onClick = onFinishClick,
                text = stringResource(Res.string.tds_finish),
                fontSize = 18.sp,
                painter = null,
                background = Color.Transparent,
                contentPadding = PaddingValues(
                    horizontal = 64.dp,
                    vertical = 16.dp
                ),
                contentColor = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .primary
            )
        }
    }
}

@Preview
@Composable
private fun DoneRestComposableContentPreview() {
    BusyBarThemeInternal {
        DelayedStartComposableContent(
            onFinishClick = {},
            onStartClick = {},
            timerSettings = TimerSettings(),
            typeEndDelay = DelayedStartScreenDecomposeComponent.TypeEndDelay.REST,
            currentIteration = 1,
            maxIteration = 3
        )
    }
}

@Preview
@Composable
private fun DoneWorkComposableContentPreview() {
    BusyBarThemeInternal {
        DelayedStartComposableContent(
            onFinishClick = {},
            onStartClick = {},
            timerSettings = TimerSettings(),
            typeEndDelay = DelayedStartScreenDecomposeComponent.TypeEndDelay.WORK,
            currentIteration = 1,
            maxIteration = 3
        )
    }
}
