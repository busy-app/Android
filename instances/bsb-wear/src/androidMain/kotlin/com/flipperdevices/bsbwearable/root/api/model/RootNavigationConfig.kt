package com.flipperdevices.bsbwearable.root.api.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface RootNavigationConfig {
    @Serializable
    data object Active : RootNavigationConfig

    @Serializable
    data object AutoPause : RootNavigationConfig

    @Serializable
    data object Finish : RootNavigationConfig

    @Serializable
    data object Card : RootNavigationConfig
}
