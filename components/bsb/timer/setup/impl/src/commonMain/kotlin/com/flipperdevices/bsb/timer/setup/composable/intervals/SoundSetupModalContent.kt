package com.flipperdevices.bsb.timer.setup.composable.intervals

import androidx.compose.foundation.layout.Arrangement
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
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_sound_on
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
import com.flipperdevices.ui.options.OptionSwitch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SoundSetupModalBottomSheetContent(
    timerSettings: TimerSettings,
    onSaveClick: () -> Unit,
    onAlertBeforeWorkStartsToggle: () -> Unit,
    onAlertBeforeWorkEndsToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Sound",
            desc = null,
            icon = painterResource(Res.drawable.ic_sound_on)
        )
        OptionSwitch(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Alert before work starts",
            infoText = "Get notified 5 seconds before your next work interval begins",
            checked = timerSettings.soundSettings.alertBeforeWorkStarts,
            onCheckChange = { onAlertBeforeWorkStartsToggle.invoke() }
        )
        OptionSwitch(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Alert before work ends",
            infoText = "Get notified 5 seconds before your current work interval ends",
            checked = timerSettings.soundSettings.alertBeforeWorkEnds,
            onCheckChange = { onAlertBeforeWorkEndsToggle.invoke() }
        )
        TimerSaveButtonComposable(onClick = onSaveClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun SoundSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        SoundSetupModalBottomSheetContent(
            timerSettings = TimerSettings(),
            onSaveClick = {},
            onAlertBeforeWorkEndsToggle = {},
            onAlertBeforeWorkStartsToggle = {}
        )
    }
}
