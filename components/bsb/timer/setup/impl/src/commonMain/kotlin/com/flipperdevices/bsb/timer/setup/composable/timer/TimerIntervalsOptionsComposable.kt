package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_long_rest
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_rest
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_work
import com.flipperdevices.ui.cardframe.SmallCardFrameComposable
import com.flipperdevices.ui.options.OptionSwitch
import com.flipperdevices.ui.timeline.toFormattedTime
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration

@Composable
fun TimerIntervalsOptionsComposable(
    isIntervalsEnabled: Boolean,
    workTime: Duration,
    restTime: Duration,
    longRestTime: Duration,
    onIntervalsToggle: () -> Unit,
    onShowWorkTimer: () -> Unit,
    onShowRestTimer: () -> Unit,
    onShowLongRestTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OptionSwitch(
            text = "Intervals",
            onCheckChange = { onIntervalsToggle.invoke() },
            checked = isIntervalsEnabled,
            infoText = "Split total time into work and rest intervals"
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallCardFrameComposable(
                title = "Work",
                desc = workTime.toFormattedTime(),
                icon = painterResource(Res.drawable.ic_work),
                onClick = onShowWorkTimer,
                modifier = Modifier.weight(1f),
                enabled = isIntervalsEnabled
            )

            SmallCardFrameComposable(
                title = "Rest",
                desc = restTime.toFormattedTime(),
                icon = painterResource(Res.drawable.ic_rest),
                onClick = onShowRestTimer,
                modifier = Modifier.weight(1f),
                enabled = isIntervalsEnabled
            )

            SmallCardFrameComposable(
                title = "Long rest",
                desc = longRestTime.toFormattedTime(),
                icon = painterResource(Res.drawable.ic_long_rest),
                onClick = onShowLongRestTimer,
                modifier = Modifier.weight(1f),
                enabled = isIntervalsEnabled
            )
        }
    }
}
