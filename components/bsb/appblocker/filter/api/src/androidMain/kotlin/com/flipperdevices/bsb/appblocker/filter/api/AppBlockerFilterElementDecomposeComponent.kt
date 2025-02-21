package com.flipperdevices.bsb.appblocker.filter.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent

abstract class AppBlockerFilterElementDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): AppBlockerFilterElementDecomposeComponent
    }
}
