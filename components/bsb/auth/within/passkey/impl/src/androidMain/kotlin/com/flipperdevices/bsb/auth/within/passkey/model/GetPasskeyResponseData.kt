package com.flipperdevices.bsb.auth.within.passkey.model

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyLoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPasskeyResponseData(
    @SerialName("response") val response: Response,
    @SerialName("authenticatorAttachment") val authenticatorAttachment: String,
    @SerialName("id") val id: String,
    @SerialName("rawId") val rawId: String,
    @SerialName("type") val type: String
) {
    @Serializable
    data class Response(
        @SerialName("clientDataJSON") val clientDataJSON: String,
        @SerialName("authenticatorData") val authenticatorData: String,
        @SerialName("signature") val signature: String,
        @SerialName("userHandle") val userHandle: String
    )
}

fun GetPasskeyResponseData.toLoginData(): BSBPasskeyLoginRequest {
    return BSBPasskeyLoginRequest(
        id = id,
        rawId = rawId,
        response = BSBPasskeyLoginRequest.Response(
            clientDataJson = response.clientDataJSON,
            authenticatorData = response.authenticatorData,
            signature = response.signature,
            userHandle = response.userHandle
        )
    )
}
