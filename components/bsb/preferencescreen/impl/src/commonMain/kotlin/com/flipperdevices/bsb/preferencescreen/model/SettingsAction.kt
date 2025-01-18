package com.flipperdevices.bsb.preferencescreen.model

sealed interface SettingsAction {
    data class SwitchDND(val newState: Boolean) : SettingsAction
    data class SwitchAppBlocking(val newState: Boolean) : SettingsAction
    data class SwitchMetronome(val newState: Boolean) : SettingsAction
    data object ExpertMode : SettingsAction
    data object OpenAuth : SettingsAction
    data object UpdateBlockedApps : SettingsAction
}
