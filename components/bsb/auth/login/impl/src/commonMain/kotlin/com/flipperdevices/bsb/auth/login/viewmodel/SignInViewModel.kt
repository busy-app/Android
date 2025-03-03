package com.flipperdevices.bsb.auth.login.viewmodel

import com.flipperdevices.bsb.auth.login.model.LoginState
import com.flipperdevices.bsb.cloud.api.BSBAuthApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.ktx.common.transform
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SignInViewModel(
    @Assisted private val email: String,
    @Assisted private val onComplete: () -> Unit,
    private val settings: PreferenceApi,
    private val bsbAuthApi: BSBAuthApi
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "SignInViewModel"

    private var state = MutableStateFlow<LoginState>(LoginState.WaitingForInput)

    fun getState() = state.asStateFlow()

    fun onLogin(password: String) = viewModelScope.launch {
        state.emit(LoginState.AuthInProgress)
        bsbAuthApi.signIn(email, password)
            .transform {
                bsbAuthApi.getUser()
            }
            .onSuccess { user ->
                settings.set(SettingsEnum.USER_DATA, user)
                withContext(Dispatchers.Main) {
                    onComplete()
                }
            }.onFailure {
                error(it) { "Failure auth for $email" }
            }
        state.emit(LoginState.WaitingForInput)
    }
}
