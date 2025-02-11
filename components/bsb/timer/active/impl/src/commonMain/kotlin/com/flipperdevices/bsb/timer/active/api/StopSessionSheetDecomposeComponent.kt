package com.flipperdevices.bsb.timer.active.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ModalDecomposeComponent

abstract class StopSessionSheetDecomposeComponent(
    componentContext: ComponentContext
) : ModalDecomposeComponent(componentContext) {
    abstract fun show()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onConfirm: () -> Unit
        ): StopSessionSheetDecomposeComponent
    }
}
