package com.flipperdevices.bsb.root.api

import androidx.compose.runtime.staticCompositionLocalOf
import com.flipperdevices.bsb.root.model.RootNavigationConfig

val LocalRootNavigation = staticCompositionLocalOf<RootNavigationInterface> {
    StubRootNavigation()
}

interface RootNavigationInterface {
    fun push(config: RootNavigationConfig)
}

// Need for working previews
private class StubRootNavigation : RootNavigationInterface {
    override fun push(config: RootNavigationConfig) {
        error("CompositionLocal LocalRootComponent not present")
    }
}
