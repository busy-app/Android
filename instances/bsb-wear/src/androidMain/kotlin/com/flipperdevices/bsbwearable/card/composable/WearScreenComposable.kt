package com.flipperdevices.bsbwearable.card.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwca_button_start
import busystatusbar.instances.bsb_wear.generated.resources.bwca_nosync
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.bsbwearable.core.ComposableWearOsScalingLazyColumn
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.compose.layout.fillMaxRectangle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@Composable
fun WearScreenComposable(
    settingsList: ImmutableList<WearOSTimerSettings>,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (settingsList.isEmpty()) {
        WearScreenEmptyComposable(
            modifier = modifier
        )
    } else {
        WearScreenContentComposable(
            timerSettings = settingsList.first(),
            onStartClick = onStartClick,
            modifier = modifier
        )
    }
}

@Composable
private fun WearScreenEmptyComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(color = 0xFF000000)) // todo
            .fillMaxRectangle(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.bwca_nosync),
            textAlign = TextAlign.Center,
            color = LocalPallet.current.white.onColor
        )
    }
}

@Composable
private fun WearScreenContentComposable(
    timerSettings: WearOSTimerSettings,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComposableWearOsScalingLazyColumn(
        modifier = modifier
            .fillMaxRectangle(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            WearCardComposable(
                modifier = Modifier
                    .fillMaxWidth(),
                settings = timerSettings
            )
        }

        item {
            BChipButton(
                modifier = Modifier
                    .padding(top = 5.dp),
                onClick = onStartClick,
                text = stringResource(Res.string.bwca_button_start),
                painter = painterResource(CommonRes.drawable.ic_play),
                contentColor = LocalCorruptedPallet.current.black.onColor,
                background = LocalCorruptedPallet.current.white.onColor,
                fontSize = 16.sp,
                iconSize = 12.dp,
                spacedBy = 8.dp,
                contentPadding = PaddingValues(
                    vertical = 12.dp,
                    horizontal = 26.dp
                ),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWearScreenComposable() {
    BusyBarThemeInternal {
        WearScreenComposable(
            settingsList = persistentListOf(
                WearOSTimerSettings(
                    instance = TimerSettings(
                        id = TimerSettingsId(id = -1),
                        intervalsSettings = TimerSettings.IntervalsSettings(isEnabled = true)
                    ),
                    blockedAppCount = BlockedAppCount.Count(count = 24)
                )
            ),
            onStartClick = {}
        )
    }
}
