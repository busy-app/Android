package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyLoginRequest
import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterChallenge
import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterRequest
import kotlinx.serialization.json.JsonElement

interface BSBPasskeyApi {
    suspend fun getLoginRequest(): Result<JsonElement>
    suspend fun passkeyAuth(request: BSBPasskeyLoginRequest): Result<Unit>

    suspend fun getRegisterRequest(): Result<BSBPasskeyRegisterChallenge>
    suspend fun passkeyRegister(request: BSBPasskeyRegisterRequest): Result<Unit>
}
