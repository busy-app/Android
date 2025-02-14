package com.flipperdevices.bsb.appblocker.permission.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class AppBlockerPermissionBlockDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): AppBlockerPermissionBlockDecomposeComponent
    }
}