package com.flipperdevices.bsbwearable.interrupt.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent

abstract class StopSessionDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {

    abstract fun show()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): StopSessionDecomposeComponent
    }
}
