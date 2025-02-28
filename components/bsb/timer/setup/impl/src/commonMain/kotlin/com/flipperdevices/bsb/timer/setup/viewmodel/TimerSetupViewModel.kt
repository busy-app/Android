package com.flipperdevices.bsb.timer.setup.viewmodel

import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.setup.store.TimerSettingsReducer
import com.flipperdevices.bsb.timer.setup.store.TimerSettingsReducer.reduce
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

    fun setTotalTime(duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings
                    .reduce(TimerSettingsReducer.Message.TotalTimeChanged(duration))
            }
        }
    }

    fun onSoundToggle() {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    soundSettings = timerSettings.soundSettings.copy(
                        alertWhenIntervalEnds = !timerSettings.soundSettings.alertWhenIntervalEnds
                    )
                )
            }
        }
    }

    fun setRest(duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.RestChanged(duration))
            }
        }
    }

    fun setWork(duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.WorkChanged(duration))
            }
        }
    }

    fun setLongRest(duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.LongRestChanged(duration))
            }
        }
    }
}
