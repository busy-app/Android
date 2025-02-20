package com.flipperdevices.bsb.appblocker.permission.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ElementDecomposeComponent
import kotlinx.coroutines.flow.StateFlow

abstract class AppBlockerPermissionBlockDecomposeComponent(
    componentContext: ComponentContext
) : ElementDecomposeComponent(componentContext) {
    abstract fun isAllPermissionGranted(): StateFlow<Boolean>

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): AppBlockerPermissionBlockDecomposeComponent
    }
}
