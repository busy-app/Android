package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.BSBEmailVerificationResponse
import com.flipperdevices.bsb.cloud.model.BSBEmailVerificationType
import com.flipperdevices.bsb.cloud.model.BSBOAuthInformation
import com.flipperdevices.bsb.cloud.model.BSBOAuthWebProvider
import com.flipperdevices.bsb.cloud.model.BSBResponse
import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.cloud.model.exception.BSBApiError
import com.flipperdevices.bsb.cloud.model.exception.BSBApiErrorException
import com.flipperdevices.bsb.cloud.model.request.BSBApiAuthCodeExchangeRequest
import com.flipperdevices.bsb.cloud.model.request.BSBApiCheckUserRequest
import com.flipperdevices.bsb.cloud.model.request.BSBApiCreateAccountRequest
import com.flipperdevices.bsb.cloud.model.request.BSBApiResetPasswordRequest
import com.flipperdevices.bsb.cloud.model.request.BSBApiSignInRequest
import com.flipperdevices.bsb.cloud.model.request.BSBCheckCodeRequest
import com.flipperdevices.bsb.cloud.model.request.BSBEmailVerificationRequest
import com.flipperdevices.bsb.cloud.model.request.BSBOneTapGoogleRequest
import com.flipperdevices.bsb.cloud.model.response.BSBApiEmailVerificationResponse
import com.flipperdevices.bsb.cloud.model.response.BSBApiSignInResponse
import com.flipperdevices.bsb.cloud.model.response.BSBApiToken
import com.flipperdevices.bsb.cloud.model.response.BSBApiUserObject
import com.flipperdevices.bsb.cloud.model.toVerificationTypeString
import com.flipperdevices.bsb.cloud.utils.NetworkConstants
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.ktx.common.transform
import com.flipperdevices.core.log.LogTagProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

internal val NETWORK_DISPATCHER = FlipperDispatchers.default

@Inject
@ContributesBinding(AppGraph::class, BSBAuthApi::class)
@Suppress("TooManyFunctions")
class BSBAuthApiImpl(
    private val httpClient: HttpClient,
    private val preferenceApi: PreferenceApi
) : BSBAuthApi, LogTagProvider {
    override val TAG = "BSBAuthApi"

    override suspend fun isUserExist(
        email: String
    ): Result<Boolean> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/sign-up/check-user")
                setBody(BSBApiCheckUserRequest(email))
            }
        }.map { true }
            .recoverCatching { exception ->
                if (exception is BSBApiErrorException &&
                    exception.errorCode == BSBApiError.UNKNOWN_USER
                ) {
                    return@withContext Result.success(false)
                }
                return@withContext Result.failure(exception)
            }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/sign-in")
                setBody(BSBApiSignInRequest(email, password))
            }.body<BSBResponse<BSBApiSignInResponse>>()
        }.transform { signIn(it.success.token.token) }
    }

    override suspend fun signIn(
        authCode: String,
        codeChallenge: String,
        codeVerification: String
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/exchange")
                setBody(
                    BSBApiAuthCodeExchangeRequest(
                        authCode = authCode,
                        codeChallenge = codeChallenge,
                        codeVerifier = codeVerification
                    )
                )
            }.body<BSBResponse<BSBApiToken>>()
        }.transform { signIn(it.success.token) }
    }

    override suspend fun signIn(token: String): Result<Unit> {
        return runCatching {
            preferenceApi.setString(SettingsEnum.AUTH_TOKEN, token)
        }.transform { getUser() }
            .onSuccess { bsbUser ->
                preferenceApi.set(SettingsEnum.USER_DATA, bsbUser)
            }.map { }
    }

    override suspend fun getUser(): Result<BSBUser> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.get {
                url("${NetworkConstants.BASE_URL}/v0/auth/me")
            }.body<BSBResponse<BSBApiUserObject>>()
        }.map { BSBUser(it.success.email) }
    }

    override suspend fun jwtAuth(token: String): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/oauth2/google/one-tap")
                setBody(BSBOneTapGoogleRequest(token))
            }.body<BSBResponse<BSBApiToken>>()
        }.onSuccess {
            preferenceApi.setString(SettingsEnum.AUTH_TOKEN, it.success.token)
        }.transform { getUser() }
            .onSuccess { bsbUser ->
                preferenceApi.set(SettingsEnum.USER_DATA, bsbUser)
            }.map { }
    }

    override suspend fun requestVerifyEmail(
        email: String,
        verificationType: BSBEmailVerificationType
    ): Result<BSBEmailVerificationResponse> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/verify-email")
                setBody(
                    BSBEmailVerificationRequest(
                        email,
                        verificationType.toVerificationTypeString()
                    )
                )
            }.body<BSBResponse<BSBApiEmailVerificationResponse>>()
        }.map {
            BSBEmailVerificationResponse(
                Instant.fromEpochSeconds(
                    Clock.System.now().epochSeconds + it.success.codeLifetime
                )
            )
        }
    }

    override suspend fun checkCode(
        email: String,
        code: String,
        verificationType: BSBEmailVerificationType
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/check-code")
                setBody(
                    BSBCheckCodeRequest(
                        email = email,
                        code = code,
                        confirmType = verificationType.toVerificationTypeString()
                    )
                )
            }
        }.map { }
    }

    override suspend fun signUp(
        email: String,
        code: String,
        password: String
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/sign-up/create-account")
                setBody(BSBApiCreateAccountRequest(email, password, code))
            }
        }.transform { signIn(email, password) }
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        password: String
    ): Result<Unit> = withContext(NETWORK_DISPATCHER) {
        return@withContext runCatching {
            httpClient.post {
                url("${NetworkConstants.BASE_URL}/v0/auth/sign-in/reset-password")
                setBody(BSBApiResetPasswordRequest(email, password, code))
            }
        }.transform { signIn(email, password) }
    }

    override fun getUrlForOauth(
        oAuthProvider: BSBOAuthWebProvider,
        codeChallenge: String
    ): BSBOAuthInformation {
        val providerKey = when (oAuthProvider) {
            BSBOAuthWebProvider.MICROSOFT -> "microsoft"
            BSBOAuthWebProvider.APPLE -> "apple"
        }
        val redirectUrl = when (oAuthProvider) {
            BSBOAuthWebProvider.MICROSOFT -> "android_deeplink_microsoft"
            BSBOAuthWebProvider.APPLE -> "android_deeplink_apple"
        }

        val url = URLBuilder("${NetworkConstants.BASE_URL}/v0/oauth2/$providerKey/sign-in").apply {
            parameters.append("redirect", redirectUrl)
            parameters.append("code_challenge", codeChallenge)
        }.toString()

        return BSBOAuthInformation(
            providerUrl = url
        )
    }
}
