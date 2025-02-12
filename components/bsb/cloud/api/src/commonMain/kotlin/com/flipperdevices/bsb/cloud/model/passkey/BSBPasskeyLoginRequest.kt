package com.flipperdevices.bsb.cloud.model.passkey

data class BSBPasskeyLoginRequest(
    val id: String,
    val rawId: String,
    val response: Response,
) {
    data class Response(
        val clientDataJson: String,
        val authenticatorData: String,
        val signature: String,
        val userHandle: String
    )
}
