package com.flipperdevices.bsb.timer.setup.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.ui.decompose.ModalDecomposeComponent

abstract class CardEditSheetDecomposeComponent(
    componentContext: ComponentContext
) : ModalDecomposeComponent(componentContext) {

    abstract fun show(timerSettingsId: TimerSettingsId)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): CardEditSheetDecomposeComponent
    }
}
