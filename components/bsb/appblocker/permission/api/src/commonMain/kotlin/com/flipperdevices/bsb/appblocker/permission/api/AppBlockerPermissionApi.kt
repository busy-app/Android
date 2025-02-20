package com.flipperdevices.bsb.appblocker.permission.api

import kotlinx.coroutines.flow.Flow

interface AppBlockerPermissionApi {
    fun isAllPermissionGranted(): Flow<Boolean>
}
