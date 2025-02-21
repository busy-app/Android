package com.flipperdevices.bsb.appblocker.card.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ElementDecomposeComponent

abstract class AppBlockerCardContentDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ): AppBlockerCardContentDecomposeComponent
    }
}
