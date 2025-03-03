package com.flipperdevices.bsbwearable.card.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class CardDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): CardDecomposeComponent
    }
}
