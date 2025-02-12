package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration

@Composable
fun TimerSetupModalBottomSheetContent(
    timerSettings: TimerSettings,
    onTotalTimeChange: (Duration) -> Unit,
    onSaveClick: () -> Unit,
    onIntervalsToggle: () -> Unit,
    onShowWorkTimer: () -> Unit,
    onShowRestTimer: () -> Unit,
    onShowLongRestTimer: () -> Unit,
    onSoundClick: () -> Unit,
    onBlockedAppsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        TimerNameTitleComposable(
            name = "BUSY",
            modifier = Modifier
                .padding(16.dp)
        )
        TotalTimeTimerPickerComposable(
            onTotalTimeChange = onTotalTimeChange,
            initialTime = timerSettings.totalTime
        )
        Spacer(Modifier.height(16.dp))
        TimerIntervalsOptionsComposable(
            isIntervalsEnabled = timerSettings.intervalsSettings.isEnabled,
            workTime = timerSettings.intervalsSettings.work,
            restTime = timerSettings.intervalsSettings.rest,
            longRestTime = timerSettings.intervalsSettings.longRest,
            onIntervalsToggle = onIntervalsToggle,
            onShowLongRestTimer = onShowLongRestTimer,
            onShowRestTimer = onShowRestTimer,
            onShowWorkTimer = onShowWorkTimer
        )
        Spacer(Modifier.height(32.dp))
        TimerSoundAppsOptionComposable(
            onSoundClick = onSoundClick,
            onBlockedAppsClick = onBlockedAppsClick
        )
        Spacer(Modifier.height(16.dp))
        TimerSaveButtonComposable(onClick = onSaveClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@Preview
private fun TimerSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        TimerSetupModalBottomSheetContent(
            onShowRestTimer = {},
            onShowWorkTimer = {},
            onBlockedAppsClick = {},
            onSoundClick = {},
            onShowLongRestTimer = {},
            onIntervalsToggle = {},
            onSaveClick = {},
            onTotalTimeChange = {},
            timerSettings = TimerSettings()
        )
    }
}
