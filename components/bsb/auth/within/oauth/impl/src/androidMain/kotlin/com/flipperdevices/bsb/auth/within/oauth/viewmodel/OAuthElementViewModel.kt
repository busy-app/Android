package com.flipperdevices.bsb.auth.within.oauth.viewmodel

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.oauth.model.InternalOAuthProvider
import com.flipperdevices.bsb.auth.within.oauth.utils.PKCEHelper
import com.flipperdevices.bsb.cloud.api.BSBAuthApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class OAuthElementViewModel(
    @Assisted private val withInStateListener: SignWithInStateListener,
    @Assisted private val oAuthProvider: InternalOAuthProvider,
    private val bsbAuthApi: BSBAuthApi,
    private val preferenceApi: PreferenceApi
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "OAuthElementViewModel"

    fun onStartAuth(context: Context) {
        val codeVerifier = PKCEHelper.generateCodeVerifier()
        preferenceApi.set(SettingsEnum.AUTH_PENDING_CODE_VERIFIER, codeVerifier)
        val codeChallenge = PKCEHelper.getCodeChallenge(codeVerifier)

        val url = bsbAuthApi.getUrlForOauth(oAuthProvider.apiProvider, codeChallenge)

        info { "Try to open ${url.providerUrl}" }

        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(context, Uri.parse(url.providerUrl))
    }

    fun onAuthCodeReceive(authCode: String) {
        info { "Receive authCode: $authCode" }
        withInStateListener(SignWithInState.InProgress(oAuthProvider.authWay))
        viewModelScope.launch {
            val pendingCode = preferenceApi.getString(SettingsEnum.AUTH_PENDING_CODE_VERIFIER, null)
            if (pendingCode == null) {
                error { "Fail to find pending code verifier for PKCE check" }
                withInStateListener(SignWithInState.WaitingForInput)
                return@launch
            }
            bsbAuthApi.signIn(
                authCode = authCode,
                codeChallenge = PKCEHelper.getCodeChallenge(pendingCode),
                codeVerification = pendingCode
            ).onSuccess {
                withInStateListener(SignWithInState.Complete)
            }.onFailure {
                error(it) { "Fail to signin with token" }
                withInStateListener(SignWithInState.WaitingForInput)
            }
        }
    }
}
