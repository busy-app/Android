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
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_work
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.ui.options.OptionSwitch
import com.flipperdevices.ui.timeline.HorizontalWheelPicker
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun RestSetupModalBottomSheetContent(
    timerSettings: TimerSettings,
    onSaveClick: () -> Unit,
    onTimeChange: (Duration) -> Unit,
    onAutoStartToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {

        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Rest",
            desc = "Pick how long you want to work during each interval",
            icon = painterResource(Res.drawable.ic_rest)
        )
        BoxWithConstraints(
            modifier = modifier.fillMaxWidth()
                .height(224.dp)
                .background(
                    LocalPallet.current
                        .transparent
                        .blackInvert
                        .secondary
                        .copy(alpha = 0.3f)
                )
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalWheelPicker(
                progression = (5.minutes.inWholeMinutes.toInt()..1.hours.inWholeMinutes.toInt() step 5.minutes.inWholeMinutes.toInt()),
                initialSelectedItem = timerSettings.intervalsSettings.rest.inWholeMinutes.toInt(),
                onItemSelect = { duration -> onTimeChange.invoke(duration) },
                unitConverter = { it.minutes }
            )
        }
        OptionSwitch(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Autostart rest",
            infoText = "Rest interval will start automatically, without manual confirmation",
            onCheckChange = { onAutoStartToggle.invoke() },
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
            timerSettings = TimerSettings(),
            onSaveClick = {},
            onAutoStartToggle = {},
            onTimeChange = {}
        )
    }
}
