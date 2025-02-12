package com.flipperdevices.bsb.profile.passkeyview.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class PasskeyViewScreenDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): PasskeyViewScreenDecomposeComponent
    }
}