package com.flipperdevices.bsbwearable.finish.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class FinishScreenDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): FinishScreenDecomposeComponent
    }
}
