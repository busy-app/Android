package com.flipperdevices.bsb.profile.passkeyview.model

sealed interface PasskeyAddState {
    data object Pending : PasskeyAddState

    data object AddInProgress : PasskeyAddState
}
