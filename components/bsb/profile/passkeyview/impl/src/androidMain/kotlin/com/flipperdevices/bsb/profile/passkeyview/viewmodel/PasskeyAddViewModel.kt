package com.flipperdevices.bsb.profile.passkeyview.viewmodel

import com.flipperdevices.bsb.auth.within.passkey.api.PasskeyApi
import com.flipperdevices.bsb.profile.passkeyview.model.PasskeyAddState
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class PasskeyAddViewModel(
    private val passkeyApi: PasskeyApi
) : DecomposeViewModel(), LogTagProvider {
    override val TAG = "PasskeyAddViewModel"

    private val state = MutableStateFlow<PasskeyAddState>(PasskeyAddState.Pending)

    fun getState() = state.asStateFlow()

    fun addPasskey() = viewModelScope.launch {
        state.emit(PasskeyAddState.AddInProgress)

        passkeyApi.registerPasskey()
            .onSuccess {
                info { "Add passkey!" }
            }
            .onFailure {
                error(it) { "Failed to add passkey" }
            }
        state.emit(PasskeyAddState.Pending)
    }
}
