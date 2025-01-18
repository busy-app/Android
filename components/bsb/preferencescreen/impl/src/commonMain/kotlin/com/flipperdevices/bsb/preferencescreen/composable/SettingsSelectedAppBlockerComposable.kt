package com.flipperdevices.bsb.preferencescreen.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SettingsSelectedAppBlockerComposable(
    blockedApps: Int,
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier
)
