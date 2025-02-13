package com.flipperdevices.bsb.appblocker.api

interface AppBlockerApi {
    fun isAppBlockerSupportActive(): Boolean
    fun enableSupport(): Result<Unit>
    fun disableSupport()
    fun count(): Int? = null
}
