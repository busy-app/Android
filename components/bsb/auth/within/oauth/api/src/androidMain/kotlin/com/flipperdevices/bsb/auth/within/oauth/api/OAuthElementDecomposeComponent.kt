package com.flipperdevices.bsb.auth.within.oauth.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.main.api.SignWithInElementDecomposeComponent
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.oauth.model.OAuthProvider

abstract class OAuthElementDecomposeComponent(
    componentContext: ComponentContext
) : SignWithInElementDecomposeComponent(componentContext) {
    abstract fun onReceiveAuthToken(token: String)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            oAuthProvider: OAuthProvider,
            withInStateListener: SignWithInStateListener
        ): OAuthElementDecomposeComponent
    }
}
