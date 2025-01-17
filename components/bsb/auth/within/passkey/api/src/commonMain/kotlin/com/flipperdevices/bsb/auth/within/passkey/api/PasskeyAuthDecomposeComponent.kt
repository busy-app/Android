package com.flipperdevices.bsb.auth.within.passkey.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.main.api.SignWithInElementDecomposeComponent
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener

abstract class PasskeyAuthDecomposeComponent(
    componentContext: ComponentContext
) : SignWithInElementDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            withInStateListener: SignWithInStateListener
        ): PasskeyAuthDecomposeComponent
    }
}
