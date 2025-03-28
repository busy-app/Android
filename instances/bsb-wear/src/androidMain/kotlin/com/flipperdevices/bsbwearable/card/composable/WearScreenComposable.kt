package com.flipperdevices.bsbwearable.card.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwca_button_start
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.rememberColumnState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun WearScreenComposable(
    settingsList: ImmutableList<WearOSTimerSettings>,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScalingLazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(color = 0xFF000000)), // todo
        columnState = rememberColumnState()
    ) {
        items(settingsList, key = { it.instance.id }) { settings ->
            WearCardComposable(
                settings = settings
            )
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
                iconSize = 12.dp,
                spacedBy = 8.dp,
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
