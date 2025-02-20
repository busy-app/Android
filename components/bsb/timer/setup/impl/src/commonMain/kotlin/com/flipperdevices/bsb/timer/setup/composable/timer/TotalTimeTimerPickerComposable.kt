package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.timeline.HorizontalWheelPicker
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Composable
fun TotalTimeTimerPickerComposable(
    initialTime: Duration,
    onTotalTimeChange: (Duration) -> Unit,
    modifier: Modifier = Modifier
) {
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
            progression = IntProgression.fromClosedRange(
                rangeStart = 15.minutes.inWholeMinutes.toInt(),
                rangeEnd = 9.hours.inWholeMinutes.toInt(),
                step = 5.minutes.inWholeMinutes.toInt()
            ),
            initialSelectedItem = initialTime,
            onItemSelect = { duration -> onTotalTimeChange.invoke(duration) },
            durationUnit = DurationUnit.MINUTES
        )
    }
}
