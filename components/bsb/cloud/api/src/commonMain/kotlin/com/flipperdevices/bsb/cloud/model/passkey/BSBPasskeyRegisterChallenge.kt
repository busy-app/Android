package com.flipperdevices.bsb.cloud.model.passkey

data class BSBPasskeyRegisterChallenge(
    val rp: Rp,
    val user: User,
    val challenge: String,
    val pubKeyCredParams: List<PubKeyCredParam>,
    val timeout: Long,
    val attestation: String,
) {
    data class Rp(
        val name: String,
        val id: String,
    )

    data class User(
        val id: String,
        val name: String,
        val displayName: String,
    )

    data class PubKeyCredParam(
        val type: String,
        val alg: Long,
    )
}
