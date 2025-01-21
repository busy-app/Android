package com.flipperdevices.bsb.cloud.api

import com.flipperdevices.bsb.cloud.model.BSBEmailVerificationResponse
import com.flipperdevices.bsb.cloud.model.BSBEmailVerificationType
import com.flipperdevices.bsb.cloud.model.BSBOAuthInformation
import com.flipperdevices.bsb.cloud.model.BSBOAuthWebProvider
import com.flipperdevices.bsb.cloud.model.BSBUser

interface BSBAuthApi {
    suspend fun isUserExist(email: String): Result<Boolean>

    suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun signIn(
        authCode: String,
        codeChallenge: String,
        codeVerification: String
    ): Result<Unit>

    suspend fun jwtAuth(token: String): Result<Unit>

    suspend fun getUser(): Result<BSBUser>

    suspend fun requestVerifyEmail(
        email: String,
        verificationType: BSBEmailVerificationType
    ): Result<BSBEmailVerificationResponse>

    suspend fun checkCode(
        email: String,
        code: String,
        verificationType: BSBEmailVerificationType
    ): Result<Unit>

    suspend fun signUp(
        email: String,
        code: String,
        password: String
    ): Result<Unit>

    suspend fun resetPassword(
        email: String,
        code: String,
        password: String
    ): Result<Unit>

    fun getUrlForOauth(oAuthProvider: BSBOAuthWebProvider, codeChallenge: String): BSBOAuthInformation
}
