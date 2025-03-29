package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import kotlinx.coroutines.flow.Flow

interface TimerSettingsApi {
    fun getTimerSettingsListFlow(): Flow<List<TimerSettings>>

    fun getTimerSettingsFlow(timerSettingsId: TimerSettingsId): Flow<TimerSettings?>

    suspend fun insert(settings: TimerSettings)
}
