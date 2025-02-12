package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.BSBResponse
import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyLoginRequest
import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterChallenge
import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterRequest
import com.flipperdevices.bsb.cloud.model.request.BSBApiPasskeyRegisterChallengeResponse
import com.flipperdevices.bsb.cloud.model.request.toApiRequest
import com.flipperdevices.bsb.cloud.model.request.toPublicApi
import com.flipperdevices.bsb.cloud.model.response.BSBApiToken
import com.flipperdevices.bsb.cloud.utils.NetworkConstants
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.transform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, BSBPasskeyApi::class)
class BSBPasskeyApiImpl(
    private val httpClient: HttpClient,
    private val authApi: BSBAuthApi
) : BSBPasskeyApi {
    override suspend fun getLoginRequest(): Result<JsonElement> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.get {
                url("${NetworkConstants.BASE_URL}/v0/auth/passkey/authentication/options")
            }.body<BSBResponse<JsonElement>>().success
        }
    }

    override suspend fun passkeyAuth(
        request: BSBPasskeyLoginRequest
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/passkey/authentication")
                setBody(request.toApiRequest())
            }.body<BSBResponse<BSBApiToken>>()
        }.transform { authApi.signIn(token = it.success.token) }
    }

    override suspend fun getRegisterRequest(): Result<BSBPasskeyRegisterChallenge> =
        withContext(NETWORK_DISPATCHER) {
            runCatching {
                httpClient.get {
                    url("${NetworkConstants.BASE_URL}/v0/auth/passkey/registration/options")
                }.body<BSBResponse<BSBApiPasskeyRegisterChallengeResponse>>().success.toPublicApi()
            }
        }

    override suspend fun passkeyRegister(
        request: BSBPasskeyRegisterRequest
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/passkey/registration")
                setBody(request.toApiRequest())
            }.body<BSBResponse<BSBApiToken>>()
        }.transform { authApi.signIn(token = it.success.token) }
    }
}
