package com.flipperdevices.bsb.auth.within.passkey.api

import android.content.Context
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import com.flipperdevices.bsb.auth.within.passkey.model.RegisterPasskeyResponseData
import com.flipperdevices.bsb.auth.within.passkey.model.toRegisterData
import com.flipperdevices.bsb.auth.within.passkey.model.toRegisterPasskeyRequest
import com.flipperdevices.bsb.cloud.api.BSBPasskeyApi
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.ktx.common.transform
import com.flipperdevices.core.log.sensitive
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, PasskeyApi::class)
class PasskeyApiImpl(
    private val context: Context,
    private val bsbPasskeyApi: BSBPasskeyApi
) : PasskeyApi {
    private val jsonParser = Json {
        ignoreUnknownKeys = true
    }

    private val credentialManager = CredentialManager.create(context)

    override suspend fun registerPasskey(): Result<Unit> = withContext(FlipperDispatchers.default) {
        bsbPasskeyApi.getRegisterRequest().mapCatching { challenge ->
            sensitive { "Receive passkey request $challenge" }
            val challengeToAndroid = Json.encodeToString(challenge.toRegisterPasskeyRequest())

            val passKeyRequest = CreatePublicKeyCredentialRequest(challengeToAndroid)

            val currentActivity = CurrentActivityHolder.getCurrentActivity()
                ?: error("Fail get current activity")

            credentialManager.createCredential(
                currentActivity,
                passKeyRequest,
            )
        }.transform { result ->
            handleRegister(result)
        }
    }

    private suspend fun handleRegister(
        result: CreateCredentialResponse
    ): Result<Unit> {
        when (result) {
            is CreatePublicKeyCredentialResponse -> {
                val passkeyResponse = jsonParser.decodeFromString<RegisterPasskeyResponseData>(
                    result.registrationResponseJson
                )
                sensitive { "Receive passkey response $passkeyResponse" }

                return bsbPasskeyApi.passkeyRegister(passkeyResponse.toRegisterData())
            }

            else -> {
                return Result.failure(RuntimeException("Unexpected type of credential: $result"))
            }
        }
    }
}
