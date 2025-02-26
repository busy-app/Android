package com.flipperdevices.bsb.timer.delayedstart.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class DelayedStartScreenDecomposeComponent(
    componentContext: ComponentContext,
) : ScreenDecomposeComponent(componentContext) {
    enum class TypeEndDelay {
        WORK, REST
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            typeEndDelay: TypeEndDelay
        ): DelayedStartScreenDecomposeComponent
    }
}
