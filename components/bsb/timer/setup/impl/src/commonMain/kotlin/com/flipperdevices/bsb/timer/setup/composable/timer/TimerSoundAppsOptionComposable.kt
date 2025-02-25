package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_error_status
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_sound_on
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_apps_card_all
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_apps_card_off
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_apps_card_permission
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_blocked_apps
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_on
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_sound_title
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.cardframe.MediumCardFrameComposable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimerSoundAppsOptionComposable(
    onSoundClick: () -> Unit,
    appBlockerState: BlockedAppCount,
    onBlockedAppsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        MediumCardFrameComposable(
            title = stringResource(Res.string.ts_bs_sound_title),
            desc = stringResource(Res.string.ts_bs_on), // todo
            icon = painterResource(Res.drawable.ic_sound_on),
            modifier = Modifier.weight(1f),
            onClick = onSoundClick,
            iconTint = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .primary
                .copy(alpha = 0.5f)
        )

        val text = when (appBlockerState) {
            BlockedAppCount.All -> stringResource(Res.string.ts_bs_apps_card_all)
            is BlockedAppCount.Count -> appBlockerState.count.toString()
            BlockedAppCount.NoPermission -> stringResource(Res.string.ts_bs_apps_card_permission)
            BlockedAppCount.TurnOff -> stringResource(Res.string.ts_bs_apps_card_off)
        }

        val icon = when (appBlockerState) {
            BlockedAppCount.All,
            is BlockedAppCount.Count,
            BlockedAppCount.TurnOff -> Res.drawable.ic_block

            BlockedAppCount.NoPermission -> Res.drawable.ic_error_status
        }

        val tint = when (appBlockerState) {
            BlockedAppCount.All,
            is BlockedAppCount.Count,
            BlockedAppCount.TurnOff ->
                LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .primary

            BlockedAppCount.NoPermission -> LocalCorruptedPallet.current.danger.primary
        }

        MediumCardFrameComposable(
            title = stringResource(Res.string.ts_bs_blocked_apps),
            desc = text,
            icon = painterResource(icon),
            modifier = Modifier.weight(1f),
            onClick = onBlockedAppsClick,
            iconTint = tint
        )
    }
}
