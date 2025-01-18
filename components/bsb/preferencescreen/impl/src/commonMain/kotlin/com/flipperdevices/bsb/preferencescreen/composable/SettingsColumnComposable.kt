package com.flipperdevices.bsb.preferencescreen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.Res
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_app_blocker_desc
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_app_blocker_title
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_metronome_title
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_notification_desc
import busystatusbar.components.bsb.preferencescreen.impl.generated.resources.preference_notification_title
import com.flipperdevices.bsb.preferencescreen.composable.debug.AuthComposable
import com.flipperdevices.bsb.preferencescreen.model.PreferenceScreenState
import com.flipperdevices.bsb.preferencescreen.model.SettingsAction

@Composable
fun SettingsColumnComposable(
    screenState: PreferenceScreenState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SettingItemComposable(
                modifier = Modifier,
                title = Res.string.preference_notification_title,
                description = Res.string.preference_notification_desc,
                enabled = screenState.isDndActive,
                onSwitch = { onAction(SettingsAction.SwitchDND(it)) }
            )

            SettingItemComposable(
                modifier = Modifier,
                title = Res.string.preference_app_blocker_title,
                description = Res.string.preference_app_blocker_desc,
                enabled = screenState.isAppBlockActive,
                onSwitch = { onAction(SettingsAction.SwitchAppBlocking(it)) }
            )

            SettingsSelectedAppBlockerComposable(
                modifier = Modifier,
                blockedApps = screenState.blockedApps ?: 0,
                onUpdate = { onAction(SettingsAction.UpdateBlockedApps) }
            )

            SettingItemComposable(
                modifier = Modifier,
                title = Res.string.preference_metronome_title,
                description = null,
                enabled = screenState.isMetronomeActive,
                onSwitch = { onAction(SettingsAction.SwitchMetronome(it)) }
            )

            if (screenState.devMode) {
                AuthComposable(
                    modifier = Modifier,
                    onClick = { onAction(SettingsAction.OpenAuth) },
                    bsbUser = screenState.bsbUser
                )
            }
        }

        SettingsVersionComposable(
            modifier = Modifier,
            onActivateExpertMode = { onAction(SettingsAction.ExpertMode) }
        )
    }
}
