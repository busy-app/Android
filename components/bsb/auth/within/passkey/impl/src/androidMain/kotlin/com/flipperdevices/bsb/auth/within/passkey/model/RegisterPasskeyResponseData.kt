package com.flipperdevices.bsb.auth.within.passkey.model

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterPasskeyResponseData(
    @SerialName("response") val response: Response,
    @SerialName("authenticatorAttachment") val authenticatorAttachment: String,
    @SerialName("id") val id: String,
    @SerialName("rawId") val rawId: String,
    @SerialName("type") val type: String
) {
    @Serializable
    data class Response(
        @SerialName("clientDataJSON") val clientDataJSON: String,
        @SerialName("attestationObject") val attestationObject: String,
        @SerialName("transports") val transports: List<String>
    )
}

fun RegisterPasskeyResponseData.toRegisterData(): BSBPasskeyRegisterRequest {
    return BSBPasskeyRegisterRequest(
        id = id,
        rawId = rawId,
        response = BSBPasskeyRegisterRequest.Response(
            clientDataJson = response.clientDataJSON,
            attestationObject = response.attestationObject,
            transports = response.transports
        )
    )
}
