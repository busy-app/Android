package com.flipperdevices.bsb.auth.within.main.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.deeplink.model.Deeplink

abstract class SignWithInMainDecomposeComponent(
    componentContext: ComponentContext
) : SignWithInElementDecomposeComponent(componentContext) {
    abstract fun handleDeeplink(deeplink: Deeplink.Root.Auth.OAuth)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            withInStateListener: SignWithInStateListener,
            deeplink: Deeplink.Root.Auth.OAuth?
        ): SignWithInMainDecomposeComponent
    }
}
