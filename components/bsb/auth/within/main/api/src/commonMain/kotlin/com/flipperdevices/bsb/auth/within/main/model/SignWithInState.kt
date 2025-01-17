package com.flipperdevices.bsb.auth.within.main.model

sealed interface SignWithInState {
    data object WaitingForInput : SignWithInState
    data class InProgress(val authWay: AuthWay) : SignWithInState
    data object Complete : SignWithInState
}

enum class AuthWay {
    EMAIL,
    GOOGLE,
    MICROSOFT,
    APPLE,
    PASSKEY
}
