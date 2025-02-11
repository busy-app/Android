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

    fun setTimer(duration: Duration) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    timer = duration
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

    fun setCycles(value: Int) {
        viewModelScope.launch {
            krateApi.timerSettingsKrate.update { timerSettings ->
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        cycles = value
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
