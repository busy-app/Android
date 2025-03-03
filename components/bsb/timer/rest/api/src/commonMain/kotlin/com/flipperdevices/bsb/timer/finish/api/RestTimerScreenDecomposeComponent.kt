package com.flipperdevices.bsb.timer.finish.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class RestTimerScreenDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {

    enum class BreakType {
        SHORT, LONG
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            breakType: BreakType
        ): RestTimerScreenDecomposeComponent
    }
}
