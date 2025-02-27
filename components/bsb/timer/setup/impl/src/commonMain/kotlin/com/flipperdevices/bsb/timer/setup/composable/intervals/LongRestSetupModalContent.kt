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
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_long_rest
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
import com.flipperdevices.ui.timeline.HorizontalWheelPicker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Composable
fun LongRestSetupModalBottomSheetContent(
    timerSettings: TimerSettings,
    onSaveClick: () -> Unit,
    onTimeChange: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Long rest",
            desc = "Pick how long you want to rest after completing 3 work intervals",
            icon = painterResource(Res.drawable.ic_long_rest)
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
                    rangeStart = 15.minutes.inWholeMinutes.toInt(),
                    rangeEnd = 30.minutes.inWholeMinutes.toInt(),
                    step = 5.minutes.inWholeMinutes.toInt()
                ),
                initialSelectedItem = timerSettings.intervalsSettings.longRest,
                onItemSelect = { duration -> onTimeChange.invoke(duration) },
                durationUnit = DurationUnit.MINUTES
            )
        }
        TimerSaveButtonComposable(onClick = onSaveClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun LongRestSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        LongRestSetupModalBottomSheetContent(
            timerSettings = TimerSettings(),
            onSaveClick = {},
            onTimeChange = {}
        )
    }
}
