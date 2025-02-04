package com.flipperdevices.bsb.auth.otp.screen.model

sealed interface AuthOtpScreenState {
    val inProgress: Boolean

    data class WaitingForInput(
        val wrongCodeInvalid: Boolean
    ) : AuthOtpScreenState {
        override val inProgress = false
    }

    data class RequestEmailInProgress(
        val launchedManually: Boolean
    ) : AuthOtpScreenState {
        override val inProgress = true
    }

    data object CheckCodeInProgress : AuthOtpScreenState {
        override val inProgress = true
    }

    data object ExpiryVerificationCode : AuthOtpScreenState {
        override val inProgress = false
    }
}
