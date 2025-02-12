package com.flipperdevices.bsb.auth.within.passkey.api

interface PasskeyApi {
    suspend fun registerPasskey(): Result<Unit>
}
