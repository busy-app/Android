package com.flipperdevices.bsb.cloud.model.request

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterChallenge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BSBApiPasskeyRegisterChallengeResponse(
    @SerialName("rp")
    val rp: Rp,
    @SerialName("user")
    val user: User,
    @SerialName("challenge")
    val challenge: String,
    @SerialName("pubKeyCredParams")
    val pubKeyCredParams: List<PubKeyCredParam>,
    @SerialName("timeout")
    val timeout: Long,
    @SerialName("hints")
    val hints: List<String>,
    @SerialName("attestation")
    val attestation: String,
) {
    @Serializable
    data class Rp(
        @SerialName("name")
        val name: String,
        @SerialName("id")
        val id: String,
    )

    @Serializable
    data class User(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("displayName")
        val displayName: String,
    )

    @Serializable
    data class PubKeyCredParam(
        @SerialName("type")
        val type: String,
        @SerialName("alg")
        val alg: Long,
    )
}

fun BSBApiPasskeyRegisterChallengeResponse.toPublicApi(): BSBPasskeyRegisterChallenge {
    return BSBPasskeyRegisterChallenge(
        rp = BSBPasskeyRegisterChallenge.Rp(
            name = rp.name,
            id = rp.id
        ),
        user = BSBPasskeyRegisterChallenge.User(
            id = user.id,
            name = user.name,
            displayName = user.displayName
        ),
        challenge = challenge,
        pubKeyCredParams = pubKeyCredParams.map { param ->
            BSBPasskeyRegisterChallenge.PubKeyCredParam(
                type = param.type,
                alg = param.alg
            )
        },
        timeout = timeout,
        attestation = attestation
    )
}
