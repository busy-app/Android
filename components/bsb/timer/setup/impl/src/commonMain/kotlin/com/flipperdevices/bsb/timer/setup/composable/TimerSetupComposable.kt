@file:OptIn(ExperimentalTime::class)

package com.flipperdevices.bsb.timer.setup.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_center_selector
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.ui.picker.NumberSelectorComposable
import com.flipperdevices.ui.picker.rememberTimerState
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

private val TOTAL_MINUTES = 1.hours.inWholeMinutes.toInt()
private val TOTAL_SECONDS = 1.minutes.inWholeSeconds.toInt()
internal const val DEFAULT_MINUTE = 25
internal const val DEFAULT_SECOND = 0

@Composable
fun TimerSetupComposable(
    onChangeTimer: suspend (TimerState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val minutesState = rememberTimerState(count = TOTAL_MINUTES, initialNumber = DEFAULT_MINUTE)
            NumberSelectorComposable(
                modifier = Modifier.fillMaxHeight(),
                count = TOTAL_MINUTES,
                pagerState = minutesState
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = ":",
                fontSize = 100.sp,
                color = LocalPallet.current.black.invert,
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                textAlign = TextAlign.Center
            )
            val secondsState = rememberTimerState(count = TOTAL_SECONDS, initialNumber = DEFAULT_SECOND)
            NumberSelectorComposable(
                modifier = Modifier.fillMaxHeight(),
                count = TOTAL_SECONDS,
                pagerState = secondsState
            )

            LaunchedEffect(minutesState, secondsState, onChangeTimer) {
                snapshotFlow {
                    TimerState(
                        minute = minutesState.currentPage.mod(TOTAL_MINUTES),
                        second = secondsState.currentPage.mod(TOTAL_SECONDS)
                    )
                }.distinctUntilChanged()
                    .collect { page ->
                        onChangeTimer(page)
                    }
            }
        }
        CenterSelectorComposable()
    }
}

@Composable
private fun CenterSelectorComposable() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.rotate(degrees = 180f),
            painter = painterResource(Res.drawable.ic_center_selector),
            contentDescription = null,
        )
        Image(
            painter = painterResource(Res.drawable.ic_center_selector),
            contentDescription = null,
        )
    }
}
