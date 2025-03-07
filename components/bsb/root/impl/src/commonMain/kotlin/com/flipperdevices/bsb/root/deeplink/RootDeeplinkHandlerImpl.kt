package com.flipperdevices.bsb.root.deeplink

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.bsb.root.api.RootDeeplinkHandler
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import com.flipperdevices.core.log.LogTagProvider

class RootDeeplinkHandlerImpl(
    private val navigation: StackNavigation<RootNavigationConfig>
) : RootDeeplinkHandler, LogTagProvider {
    override val TAG = "RootDeeplinkHandler"

    override fun handleDeeplink(deeplink: Deeplink) {
        navigation.bringToFront(getConfigFromDeeplink(deeplink))
    }

    companion object {
        fun getConfigFromDeeplink(deeplink: Deeplink): RootNavigationConfig {
            return when (deeplink) {
                is Deeplink.Root.Auth -> RootNavigationConfig.Profile(deeplink)
            }
        }
    }
}
