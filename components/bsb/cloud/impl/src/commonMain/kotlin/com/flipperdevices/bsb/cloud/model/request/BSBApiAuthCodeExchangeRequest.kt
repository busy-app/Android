package com.flipperdevices.bsb.cloud.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BSBApiAuthCodeExchangeRequest(
    @SerialName("auth_code")
    val authCode: String,
    @SerialName("code_challenge")
    val codeChallenge: String,
    @SerialName("code_verifier")
    val codeVerifier: String
)
