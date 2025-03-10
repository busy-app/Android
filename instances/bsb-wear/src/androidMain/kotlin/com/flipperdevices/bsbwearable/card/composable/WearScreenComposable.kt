package com.flipperdevices.bsbwearable.card.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.placeholderShimmer
import androidx.wear.compose.material.rememberPlaceholderState
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwca_button_start
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.core.ktx.common.placeholder
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.rememberColumnState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearMaterialApi::class)
@Composable
fun WearScreenComposable(
    settings: TimerSettings?,
    blockerState: BlockedAppCount?,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScalingLazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(color = 0xFF000000)), // todo
        columnState = rememberColumnState()
    ) {
        item {
            if (settings != null) {
                WearCardComposable(
                    settings = settings,
                    blockerState = blockerState,
                )
            } else {
                Box(modifier = Modifier.padding(vertical = 24.dp)) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(54.dp),
                        indicatorColor = LocalCorruptedPallet.current.accent.device.secondary
                    )
                }
            }

        }
        item {
            BChipButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                onClick = onStartClick,
                text = stringResource(Res.string.bwca_button_start),
                painter = painterResource(CommonRes.drawable.ic_play),
                contentColor = LocalCorruptedPallet.current.black.onColor,
                background = LocalCorruptedPallet.current.white.onColor,
                fontSize = 16.sp,
                contentPadding = PaddingValues(
                    vertical = 12.dp,
                    horizontal = 14.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWearScreenComposable() {
    BusyBarThemeInternal {
        WearScreenComposable(
            settings = TimerSettings(
                intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = true)
            ),
            blockerState = BlockedAppCount.Count(count = 24),
            onStartClick = {}
        )
    }
}
