package com.flipperdevices.bsbwearable.root.api.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface RootNavigationConfig {
    data object HelloWord : RootNavigationConfig
}
