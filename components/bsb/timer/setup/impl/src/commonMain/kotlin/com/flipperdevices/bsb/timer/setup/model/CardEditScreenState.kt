package com.flipperdevices.bsb.timer.setup.model

import com.flipperdevices.bsb.dao.model.TimerSettings

sealed interface CardEditScreenState {
    data object NotInitialized : CardEditScreenState

    data class Loaded(val timerSettings: TimerSettings?) : CardEditScreenState
}