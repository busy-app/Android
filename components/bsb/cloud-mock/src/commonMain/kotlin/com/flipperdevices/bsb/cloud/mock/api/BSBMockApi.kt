package com.flipperdevices.bsb.cloud.mock.api

import com.flipperdevices.bsb.cloud.model.response.BSBApiUserObject
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp

interface BSBMockApi {
    suspend fun auth(): Result<BSBApiUserObject>

    suspend fun saveTimer(timestamp: TimerTimestamp): Result<TimerTimestamp>

    suspend fun getTimer(): Result<TimerTimestamp>
    companion object {
        const val HOST_URL = "http://192.168.0.108:8080"
        const val BASE_URL = "${HOST_URL}/api"
    }
}
