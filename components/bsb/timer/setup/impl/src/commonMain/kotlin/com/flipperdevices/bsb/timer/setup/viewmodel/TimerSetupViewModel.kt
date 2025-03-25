package com.flipperdevices.bsb.timer.setup.viewmodel

import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.setup.model.CardEditScreenState
import com.flipperdevices.bsb.timer.setup.store.TimerSettingsReducer
import com.flipperdevices.bsb.timer.setup.store.TimerSettingsReducer.reduce
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration

@Inject
class TimerSetupViewModel(
    @Assisted private val timerSettingsId: TimerSettingsId,
    private val timerSettingsApi: TimerSettingsApi,
) : DecomposeViewModel() {
    private val timerSettingsStateFlow = timerSettingsApi
        .getTimerSettingsFlow(timerSettingsId)
        .map { CardEditScreenState.Loaded(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            CardEditScreenState.NotInitialized
        )

    fun getState() = timerSettingsStateFlow

    fun toggleWorkAutoStart(timerSettings: TimerSettings) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        autoStartWork = !timerSettings.intervalsSettings.autoStartWork
                    )
                )
            )
        }
    }

    fun toggleRestAutoStart(timerSettings: TimerSettings) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        autoStartRest = !timerSettings.intervalsSettings.autoStartRest
                    )
                )
            )
        }
    }

    fun toggleIntervals(timerSettings: TimerSettings) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings.copy(
                    intervalsSettings = timerSettings.intervalsSettings.copy(
                        isEnabled = !timerSettings.intervalsSettings.isEnabled
                    )
                )
            )
        }
    }

    fun setTotalTime(
        timerSettings: TimerSettings, duration: Duration
    ) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings
                    .reduce(TimerSettingsReducer.Message.TotalTimeChanged(duration))
            )
        }
    }

    fun onSoundToggle(timerSettings: TimerSettings) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings.copy(
                    soundSettings = timerSettings.soundSettings.copy(
                        alertWhenIntervalEnds = !timerSettings.soundSettings.alertWhenIntervalEnds
                    )
                )
            )
        }
    }

    fun setRest(timerSettings: TimerSettings, duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.RestChanged(duration))
            )
        }
    }

    fun setWork(timerSettings: TimerSettings, duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.WorkChanged(duration))
            )
        }
    }

    fun setLongRest(timerSettings: TimerSettings, duration: Duration) = with(TimerSettingsReducer) {
        viewModelScope.launch {
            timerSettingsApi.insert(
                timerSettings
                    .reduce(TimerSettingsReducer.Message.Interval.LongRestChanged(duration))
            )
        }
    }
}
