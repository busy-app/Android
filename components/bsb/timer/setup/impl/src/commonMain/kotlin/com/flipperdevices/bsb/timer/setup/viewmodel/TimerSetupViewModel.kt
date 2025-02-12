package com.flipperdevices.bsb.timer.setup.viewmodel

import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ru.astrainteractive.klibs.kstorage.util.KrateExt.update
import kotlin.time.Duration

@Inject
class TimerSetupViewModel(
    private val krateApi: KrateApi
) : DecomposeViewModel() {
    val state = krateApi.timerSettingsKrate.stateFlow(
        coroutineScope = viewModelScope,
        sharingStarted = SharingStarted.Eagerly
    )

    fun toggleWorkAutoStart() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        autoStartWork = !timerSettings.intervalsSettings.autoStartWork
                    )
                )
            }
        }
    }

    fun toggleRestAutoStart() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        autoStartRest = !timerSettings.intervalsSettings.autoStartRest
                    )
                )
            }
        }
    }

    fun toggleSoundBeforeWorkStarts() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    soundSettings = timerSettings.soundSettings.copy(
                        alertBeforeWorkStarts = !timerSettings.soundSettings.alertBeforeWorkStarts
                    )
                )
            }
        }
    }

    fun toggleSoundBeforeWorkEnds() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    soundSettings = timerSettings.soundSettings.copy(
                        alertBeforeWorkEnds = !timerSettings.soundSettings.alertBeforeWorkEnds
                    )
                )
            }
        }
    }

    fun toggleIntervals() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        isEnabled = !timerSettings.intervalsSettings.isEnabled
                    )
                )
            }
        }
    }

    fun setTotalTime(duration: Duration) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    totalTime = duration
                )
            }
        }
    }

    fun setRest(duration: Duration) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        rest = duration
                    )
                )
            }
        }
    }

    fun setWork(duration: Duration) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        work = duration
                    )
                )
            }
        }
    }

    fun setLongRest(duration: Duration) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        longRest = duration
                    )
                )
            }
        }
    }

    fun setIntervalsEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        isEnabled = isEnabled
                    )
                )
            }
        }
    }
}
