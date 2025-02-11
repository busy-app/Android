package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyLoginRequest
import kotlinx.serialization.json.JsonElement

interface BSBPasskeyApi {
    suspend fun getLoginRequest(): Result<JsonElement>
    suspend fun passkeyAuth(request: BSBPasskeyLoginRequest): Result<Unit>
}