package com.flipperdevices.bsb.timer.setup.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ModalDecomposeComponent

abstract class TimerSetupSheetDecomposeComponent(
    componentContext: ComponentContext
) : ModalDecomposeComponent(componentContext) {

    abstract fun show()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): TimerSetupSheetDecomposeComponent
    }
}
