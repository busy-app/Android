package com.flipperdevices.bsb.profile.main.model

import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.deeplink.model.Deeplink
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavigationConfig {
    @Serializable
    data class Main(val user: BSBUser) : ProfileNavigationConfig

    @Serializable
    data class Login(val deeplink: Deeplink.Root.Auth?) : ProfileNavigationConfig
}
