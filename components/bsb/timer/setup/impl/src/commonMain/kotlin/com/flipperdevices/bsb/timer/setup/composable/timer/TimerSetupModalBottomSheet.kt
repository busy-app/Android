package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.intervals.SoundSetupModalBottomSheetContent
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
    onSoundToggle: () -> Unit,
    appBlockerCardContent: @Composable () -> Unit,
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
            name = "BUSY", // todo raw string
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
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LocalCorruptedPallet.current.transparent.whiteInvert.quinary)
                .padding(12.dp),
            content = { appBlockerCardContent.invoke() }
        )
        Spacer(Modifier.height(8.dp))
        SoundSetupModalBottomSheetContent(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LocalCorruptedPallet.current.transparent.whiteInvert.quinary)
                .clickable { onSoundToggle.invoke() }
                .padding(12.dp),
            isChecked = timerSettings.soundSettings.alertWhenIntervalEnds,
            onChange = onSoundToggle,
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
            onShowLongRestTimer = {},
            onIntervalsToggle = {},
            onSaveClick = {},
            onTotalTimeChange = {},
            timerSettings = TimerSettings(),
            appBlockerCardContent = {},
            onSoundToggle = {}
        )
    }
}
