package com.flipperdevices.bsb.cloud.model.request

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BSBApiPasskeyRegisterRequest(
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
        @SerialName("attestation_object")
        val attestationObject: String,
        @SerialName("transports")
        val transports: List<String>
    )
}

fun BSBPasskeyRegisterRequest.toApiRequest(): BSBApiPasskeyRegisterRequest {
    return BSBApiPasskeyRegisterRequest(
        id = id,
        rawId = rawId,
        response = BSBApiPasskeyRegisterRequest.Response(
            clientDataJson = response.clientDataJson,
            attestationObject = response.attestationObject,
            transports = response.transports
        )
    )
}
