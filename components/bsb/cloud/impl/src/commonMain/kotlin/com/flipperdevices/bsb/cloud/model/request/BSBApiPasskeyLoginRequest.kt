package com.flipperdevices.bsb.cloud.model.request

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyLoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BSBApiPasskeyLoginRequest(
    @SerialName("id")
    val id: String,
    @SerialName("raw_id")
    val rawId: String,
    @SerialName("response")
    val response: Response,
    @SerialName("authenticator_attachment")
    val authenticatorAttachment: String = "platform",
    @SerialName("type")
    val type: String = "public-key"
) {
    @Serializable
    data class Response(
        @SerialName("client_data_json")
        val clientDataJson: String,
        @SerialName("authenticator_data")
        val authenticatorData: String,
        @SerialName("signature")
        val signature: String,
        @SerialName("user_handle")
        val userHandle: String
    )
}

fun BSBPasskeyLoginRequest.toApiRequest(): BSBApiPasskeyLoginRequest {
    return BSBApiPasskeyLoginRequest(
        id = id,
        rawId = rawId,
        response = BSBApiPasskeyLoginRequest.Response(
            clientDataJson = response.clientDataJson,
            authenticatorData = response.authenticatorData,
            signature = response.signature,
            userHandle = response.userHandle
        )
    )
}
