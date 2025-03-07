package com.flipperdevices.bsb.root.model

import com.flipperdevices.bsb.deeplink.model.Deeplink
import kotlinx.serialization.Serializable

@Serializable
sealed interface RootNavigationConfig {
    @Serializable
    data object Timer : RootNavigationConfig

    @Serializable
    data class Profile(val deeplink: Deeplink.Root.Auth?) : RootNavigationConfig
}
