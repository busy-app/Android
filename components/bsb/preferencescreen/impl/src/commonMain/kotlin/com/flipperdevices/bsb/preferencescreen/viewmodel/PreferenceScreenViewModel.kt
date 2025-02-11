package com.flipperdevices.bsb.preferencescreen.viewmodel

import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.dnd.api.BusyDNDApi
import com.flipperdevices.bsb.metronome.api.MetronomeApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.getFlow
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.bsb.preferencescreen.model.PreferenceScreenState
import com.flipperdevices.bsb.preferencescreen.model.SettingsAction
import com.flipperdevices.bsb.root.api.RootNavigationInterface
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@Inject
class PreferenceScreenViewModel(
    private val dndApi: BusyDNDApi,
    private val appBlockerApi: AppBlockerApi,
    private val preferenceApi: PreferenceApi,
    private val metronomeApi: MetronomeApi
) : DecomposeViewModel() {
    private val state =
        MutableStateFlow(
            PreferenceScreenState(
                isDndActive = dndApi.isDNDSupportActive(),
                isAppBlockActive = appBlockerApi.isAppBlockerSupportActive(),
                isMetronomeActive = metronomeApi.isMetronomeSupportActive(),
                devMode = preferenceApi.get(SettingsEnum.DEV_MODE, false),
                bsbUser = preferenceApi.get<BSBUser?>(SettingsEnum.USER_DATA, null),
                blockedApps = appBlockerApi.count()
            )
        )

    init {
        combine(
            preferenceApi.getFlow(SettingsEnum.DEV_MODE, false),
            preferenceApi.getFlow<BSBUser?>(SettingsEnum.USER_DATA, null)
        ) { devMode, bsbUser ->
            state.update { it.copy(devMode = devMode, bsbUser = bsbUser) }
        }.launchIn(viewModelScope)
    }

    fun getState() = state.asStateFlow()

    fun onAction(
        action: SettingsAction,
        navigation: RootNavigationInterface
    ) {
        when (action) {
            SettingsAction.ExpertMode -> preferenceApi.set(SettingsEnum.DEV_MODE, true)
            SettingsAction.OpenAuth -> navigation.push(RootNavigationConfig.Profile(null))
            is SettingsAction.SwitchAppBlocking -> onAppBlock(action.newState)
            is SettingsAction.SwitchDND -> onDndSwitch(action.newState)
            is SettingsAction.SwitchMetronome -> onMetronomeSwitch(action.newState)
            SettingsAction.UpdateBlockedApps -> onUpdateBlockedApps()
        }
    }

    private fun onMetronomeSwitch(newState: Boolean) {
        if (newState) {
            metronomeApi.enableSupport()
        } else {
            metronomeApi.disableSupport()
        }
        state.update {
            it.copy(isMetronomeActive = metronomeApi.isMetronomeSupportActive())
        }
    }

    private fun onUpdateBlockedApps() {
        state.update {
            it.copy(blockedApps = appBlockerApi.count())
        }
    }

    private fun onDndSwitch(newState: Boolean) {
        if (newState) {
            dndApi.enableSupport()
        } else {
            dndApi.disableSupport()
        }
        state.update {
            it.copy(isDndActive = dndApi.isDNDSupportActive())
        }
    }

    private fun onAppBlock(newState: Boolean) {
        if (newState) {
            appBlockerApi.enableSupport()
        } else {
            appBlockerApi.disableSupport()
        }
        state.update {
            it.copy(isAppBlockActive = appBlockerApi.isAppBlockerSupportActive())
        }
    }
}
