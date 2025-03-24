package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlinx.coroutines.flow.Flow

interface TimerSettingsApi {
    fun getTimerSettingsFlow(): Flow<List<TimerSettings>>

    suspend fun insert(settings: TimerSettings)
}