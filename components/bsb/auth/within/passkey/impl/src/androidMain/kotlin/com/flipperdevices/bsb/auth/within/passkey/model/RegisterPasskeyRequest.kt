package com.flipperdevices.bsb.auth.within.passkey.model

import com.flipperdevices.bsb.cloud.model.passkey.BSBPasskeyRegisterChallenge
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterPasskeyRequest(
    @SerialName("challenge")
    val challenge: String,
    @SerialName("rp")
    val rp: Rp,
    @SerialName("user")
    val user: User,
    @SerialName("pubKeyCredParams")
    val pubKeyCredParams: List<PubKeyCredParam>,
    @SerialName("timeout")
    val timeout: Long,
    @SerialName("attestation")
    val attestation: String,
    @SerialName("excludeCredentials")
    val excludeCredentials: List<ExcludeCredential>,
    @SerialName("authenticatorSelection")
    val authenticatorSelection: AuthenticatorSelection
) {
    @Serializable
    data class Rp(
        @SerialName("name")
        val name: String,
        @SerialName("id")
        val id: String
    )

    @Serializable
    data class User(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
        @SerialName("displayName")
        val displayName: String
    )

    @Serializable
    data class PubKeyCredParam(
        @SerialName("type")
        val type: String,
        @SerialName("alg")
        val alg: Long
    )

    @Serializable
    data class AuthenticatorSelection(
        @SerialName("authenticatorAttachment")
        val authenticatorAttachment: String,
        @SerialName("requireResidentKey")
        val requireResidentKey: Boolean,
        @SerialName("residentKey")
        val residentKey: String,
        @SerialName("userVerification")
        val userVerification: String
    )

    @Serializable
    data class ExcludeCredential(
        @SerialName("id")
        val id: String,
        @SerialName("type")
        val type: String
    )
}

fun BSBPasskeyRegisterChallenge.toRegisterPasskeyRequest(): RegisterPasskeyRequest {
    return RegisterPasskeyRequest(
        challenge = challenge,
        rp = RegisterPasskeyRequest.Rp(
            name = rp.name,
            id = "cloud.dev.busy.bar"
        ),
        user = RegisterPasskeyRequest.User(
            id = user.id,
            name = user.name,
            displayName = user.displayName
        ),
        pubKeyCredParams = pubKeyCredParams.map { param ->
            RegisterPasskeyRequest.PubKeyCredParam(
                type = param.type,
                alg = param.alg
            )
        },
        timeout = timeout,
        attestation = attestation,
        excludeCredentials = listOf(),
        authenticatorSelection = RegisterPasskeyRequest.AuthenticatorSelection(
            authenticatorAttachment = "platform",
            requireResidentKey = false,
            residentKey = "required",
            userVerification = "required"
        )
    )
}
