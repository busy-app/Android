package com.flipperdevices.bsb.timer.active.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceIn
import busystatusbar.components.bsb.timer.active.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.active.impl.generated.resources.busy_fire_first_frame
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.ui.timeline.util.toFormattedTime
import com.flipperdevices.ui.video.BSBVideoPlayer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val HD_RATIO = 16f / 9f

@Composable
@OptIn(ExperimentalResourceApi::class)
fun TimerCardComposable(
    timerState: ControlledTimerState.Running,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(24.dp))
    ) {
        var heightPx by remember { mutableStateOf<Int?>(null) }
        BSBVideoPlayer(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(HD_RATIO)
                .alpha(0.9f)
                .onGloballyPositioned { coordinates ->
                    heightPx = coordinates.size.height
                },
            uri = Res.getUri("files/busy_fire.mp4"),
            firstFrame = Res.drawable.busy_fire_first_frame
        )

        var columnModifier: Modifier = Modifier
        heightPx?.let { heightPxLocal ->
            columnModifier = columnModifier.height(LocalDensity.current.run { heightPxLocal.toDp() })
        }
        Column(
            modifier = columnModifier
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                timerState.timeLeft.toComponents { days, hours, minutes, seconds, nanoseconds ->
                    val timeComponentList = listOfNotNull(
                        hours.takeIf { h -> h > 0 },
                        minutes,
                        seconds
                    )
                    Text(
                        text = timeComponentList.joinToString(
                            separator = ":",
                            prefix = "",
                            transform = { timeComponent -> timeComponent.toFormattedTime() }
                        ),
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                            color = LocalCorruptedPallet.current.white.invert
                        )
                    )
                }
            }

            val totalDuration = remember(timerState.timerSettings.intervalsSettings) {
                when (timerState) {
                    is ControlledTimerState.Running.LongRest -> timerState.timerSettings.intervalsSettings.longRest
                    is ControlledTimerState.Running.Rest -> timerState.timerSettings.intervalsSettings.rest
                    is ControlledTimerState.Running.Work -> timerState.timerSettings.intervalsSettings.work
                }
            }
            val progress = remember(timerState.timeLeft, totalDuration) {
                1f - (timerState.timeLeft / totalDuration).toFloat().fastCoerceIn(0f, 1f)
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.5.dp)
                    .height(6.dp),
                progress = progress,
                color = LocalCorruptedPallet.current.accent.brand.primary,
                backgroundColor = LocalCorruptedPallet.current.accent.brand.tertiary
            )
        }
    }
}

@Composable
@Preview
private fun TimerCardComposablePreview() {
    BusyBarThemeInternal {
        TimerCardComposable(
            timerState = ControlledTimerState.Running.Work(
                timeLeft = 13.minutes.plus(10.seconds),
                isOnPause = false,
                currentIteration = 1,
                maxIterations = 4,
                timerSettings = TimerSettings()
            )
        )
    }
}