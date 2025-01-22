package com.flipperdevices.bsb.auth.main.model

import com.flipperdevices.bsb.deeplink.model.Deeplink
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRootNavigationConfig {
    @Serializable
    data class AuthRoot(val deeplink: Deeplink.Root.Auth.OAuth?) : AuthRootNavigationConfig

    @Serializable
    data class LogIn(
        val email: String,
        val preFilledPassword: String?,
        val deeplink: Deeplink.Root.Auth.VerifyEmailLink.ResetPassword?
    ) : AuthRootNavigationConfig

    @Serializable
    data class SignUp(
        val email: String,
        val preFilledPassword: String?,
        val deeplink: Deeplink.Root.Auth.VerifyEmailLink.SignUp?
    ) : AuthRootNavigationConfig
}
