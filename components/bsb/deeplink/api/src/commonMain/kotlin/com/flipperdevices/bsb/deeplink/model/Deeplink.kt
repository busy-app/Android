package com.flipperdevices.bsb.deeplink.model

import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import kotlinx.serialization.Serializable

@Serializable
sealed interface Deeplink {

    @Serializable
    sealed interface Root : Deeplink {
        @Serializable
        data class AppLockScreen(val applicationInfo: ApplicationInfo) : Root

        @Serializable
        sealed interface Auth : Root {

            @Serializable
            sealed interface OAuth : Auth {
                val authCode: String

                @Serializable
                data class Microsoft(override val authCode: String) : OAuth

                @Serializable
                data class Apple(override val authCode: String) : OAuth
            }

            @Serializable
            sealed interface VerifyEmailLink : Auth {
                val otpCode: String
                val email: String

                @Serializable
                data class ResetPassword(
                    override val otpCode: String,
                    override val email: String,
                ) : VerifyEmailLink

                @Serializable
                data class SignUp(
                    override val otpCode: String,
                    override val email: String,
                ) : VerifyEmailLink
            }
        }
    }
}
