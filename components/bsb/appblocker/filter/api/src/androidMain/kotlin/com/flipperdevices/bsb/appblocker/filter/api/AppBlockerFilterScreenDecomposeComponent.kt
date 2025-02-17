package com.flipperdevices.bsb.appblocker.filter.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class AppBlockerFilterScreenDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ): AppBlockerFilterScreenDecomposeComponent
    }
}