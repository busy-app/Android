package com.flipperdevices.bsb.auth.within.passkey.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.flipperdevices.bsb.auth.within.main.model.AuthWay
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.core.ktx.common.transform
import com.flipperdevices.core.log.error
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PasskeyViewModel(
    @Assisted private val withInStateListener: SignWithInStateListener,
    private val context: Context
) : DecomposeViewModel() {
    private val credentialManager = CredentialManager.create(context)

    fun onAuth() = viewModelScope.launch {
        withInStateListener(SignWithInState.InProgress(AuthWay.PASSKEY))

        val request = GetCredentialRequest.Builder()
            .build()

        runCatching {
            credentialManager.getCredential(
                request = request,
                context = context,
            )
        }.transform { _ ->
            Result.failure<Unit>(Throwable())
            //handleSignIn(result)
        }.onSuccess {
            withInStateListener(SignWithInState.Complete)
        }.onFailure {
            error(it) { "Failed sign in with google" }
            withInStateListener(SignWithInState.WaitingForInput)
        }
    }

    fun interface Factory {
        operator fun invoke(
            withInStateListener: SignWithInStateListener
        ): PasskeyViewModel
    }
}