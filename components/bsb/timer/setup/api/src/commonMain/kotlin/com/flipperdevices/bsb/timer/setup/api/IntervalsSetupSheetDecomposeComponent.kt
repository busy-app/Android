package com.flipperdevices.bsb.timer.setup.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ModalDecomposeComponent

abstract class IntervalsSetupSheetDecomposeComponent(
    componentContext: ComponentContext
) : ModalDecomposeComponent(componentContext) {

    abstract fun showRest()

    abstract fun showLongRest()

    abstract fun showCycles()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): IntervalsSetupSheetDecomposeComponent
    }
}
