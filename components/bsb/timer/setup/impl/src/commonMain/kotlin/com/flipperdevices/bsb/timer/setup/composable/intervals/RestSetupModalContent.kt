package com.flipperdevices.bsb.timer.setup.composable.intervals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_rest
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_rest_autostart_desc
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_rest_autostart_title
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_rest_desc
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_rest_title
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
import com.flipperdevices.ui.options.OptionSwitch
import com.flipperdevices.ui.timeline.HorizontalWheelPicker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Composable
fun RestSetupModalBottomSheetContent(
    timerSettings: TimerSettings,
    onSaveClick: () -> Unit,
    onTimeChange: (TimerSettings, Duration) -> Unit,
    onAutoStartToggle: (TimerSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(Res.string.ts_bs_rest_title),
            desc = stringResource(Res.string.ts_bs_rest_desc),
            icon = painterResource(Res.drawable.ic_rest)
        )
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
                .height(224.dp)
                .background(
                    LocalCorruptedPallet.current
                        .transparent
                        .blackInvert
                        .secondary
                        .copy(alpha = 0.3f)
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalWheelPicker(
                progression = IntProgression.fromClosedRange(
                    rangeStart = 5.minutes.inWholeMinutes.toInt(),
                    rangeEnd = 1.hours.inWholeMinutes.toInt(),
                    step = 5.minutes.inWholeMinutes.toInt()
                ),
                initialSelectedItem = timerSettings.intervalsSettings.rest,
                onItemSelect = { duration -> onTimeChange.invoke(timerSettings, duration) },
                durationUnit = DurationUnit.MINUTES
            )
        }
        OptionSwitch(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.ts_bs_rest_autostart_title),
            infoText = stringResource(Res.string.ts_bs_rest_autostart_desc),
            onCheckChange = { onAutoStartToggle.invoke(timerSettings) },
            checked = timerSettings.intervalsSettings.autoStartRest
        )
        TimerSaveButtonComposable(onClick = onSaveClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun RestSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        RestSetupModalBottomSheetContent(
            timerSettings = TimerSettings(TimerSettingsId(id = -1)),
            onSaveClick = {},
            onAutoStartToggle = {},
            onTimeChange = { _, _ -> }
        )
    }
}
