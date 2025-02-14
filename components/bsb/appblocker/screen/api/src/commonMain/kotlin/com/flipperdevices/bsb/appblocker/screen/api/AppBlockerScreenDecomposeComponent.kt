package com.flipperdevices.bsb.appblocker.screen.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class AppBlockerScreenDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            applicationInfo: ApplicationInfo,
            onBackParameter: DecomposeOnBackParameter
        ): AppBlockerScreenDecomposeComponent
    }
}
