package com.flipperdevices.bsb.cloud.model.passkey

data class BSBPasskeyRegisterRequest(
    val id: String,
    val rawId: String,
    val response: Response,
) {
    data class Response(
        val clientDataJson: String,
        val attestationObject: String,
        val transports: List<String>
    )
}
