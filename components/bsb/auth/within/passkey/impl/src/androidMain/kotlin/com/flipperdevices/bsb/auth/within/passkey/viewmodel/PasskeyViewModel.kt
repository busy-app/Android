package com.flipperdevices.bsb.auth.within.passkey.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import com.flipperdevices.bsb.auth.within.main.model.AuthWay
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.passkey.model.GetPasskeyResponseData
import com.flipperdevices.bsb.auth.within.passkey.model.toLoginData
import com.flipperdevices.bsb.cloud.api.BSBPasskeyApi
import com.flipperdevices.core.ktx.common.transform
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.sensitive
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PasskeyViewModel(
    @Assisted private val withInStateListener: SignWithInStateListener,
    private val bsbPasskeyApi: BSBPasskeyApi,
    private val context: Context
) : DecomposeViewModel() {
    private val credentialManager = CredentialManager.create(context)

    fun onAuth() = viewModelScope.launch {
        withInStateListener(SignWithInState.InProgress(AuthWay.PASSKEY))

        bsbPasskeyApi.getLoginRequest().mapCatching { jsonElement ->
            sensitive { "Receive passkey request $jsonElement" }

            val passKeyRequest = GetPublicKeyCredentialOption(Json.encodeToString(jsonElement))

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(passKeyRequest)
                .build()

            credentialManager.getCredential(
                request = request,
                context = context,
            )
        }.transform { result ->
            handleSignIn(result)
        }.onSuccess {
            withInStateListener(SignWithInState.Complete)
        }.onFailure {
            error(it) { "Failed sign in with google" }
            withInStateListener(SignWithInState.WaitingForInput)
        }
    }

    private suspend fun handleSignIn(
        result: GetCredentialResponse
    ): Result<Unit> {
        when (val credential = result.credential) {
            is PublicKeyCredential -> {
                try {
                    val passkeyResponse = Json.decodeFromString<GetPasskeyResponseData>(
                        credential.authenticationResponseJson
                    )
                    sensitive { "Receive passkey response $passkeyResponse" }

                    return bsbPasskeyApi.passkeyAuth(passkeyResponse.toLoginData())
                } catch (e: GoogleIdTokenParsingException) {
                    return Result.failure(
                        RuntimeException(
                            "Received an invalid google id token response",
                            e
                        )
                    )
                }
            }

            else -> {
                return Result.failure(RuntimeException("Unexpected type of credential: $credential"))
            }
        }
    }
}
