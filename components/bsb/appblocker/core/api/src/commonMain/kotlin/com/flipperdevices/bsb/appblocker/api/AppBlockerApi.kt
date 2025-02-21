package com.flipperdevices.bsb.appblocker.api

import kotlinx.coroutines.flow.Flow

interface AppBlockerApi {
    fun isAppBlockerSupportActive(): Flow<Boolean>
    fun enableSupport(): Result<Unit>
    fun disableSupport()
    fun count(): Int? = null
}
