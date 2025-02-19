package com.flipperdevices.bsb.appblocker.filter.api

interface AppBlockerFilterApi {
    suspend fun isBlocked(packageName: String): Boolean
}