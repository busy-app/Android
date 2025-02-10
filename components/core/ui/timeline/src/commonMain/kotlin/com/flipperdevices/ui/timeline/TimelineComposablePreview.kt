package com.flipperdevices.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun TimelineComposablePreview(
    currentValue: Duration = 75.seconds,
    range: ClosedRange<Int> = 0..9.hours.inWholeSeconds.toInt(),
    numSegments: Int = 5,
    density: Int = 10
) {
    BusyBarThemeInternal {
        val map = remember { HashMap<Int, AnimatedTextState>() }
        val state = rememberPodcastSliderState(
            currentValue = currentValue.inWholeSeconds.toFloat(),
            range = range
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
            PodcastSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                density = with(LocalDensity.current) { maxWidth.toPx() / density.dp.toPx() }.toInt(),
                numSegments = numSegments,
                state = state,
                minAlpha = 1f,
                indicatorLabel = { alpha, value ->
                    if (value % 5 == 0) {
                        Box(
                            modifier = Modifier.height(with(LocalDensity.current) { 85.sp.toDp() }),
                            contentAlignment = Alignment.Center
                        ) {
                            TextContent(
                                value = value,
                                selected = state.selected.value,
                                map = map
                            )
                        }
                    }
                }
            )
        }
    }
}
