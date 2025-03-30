package com.flipperdevices.bsb.timer.setup.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.ui.decompose.ModalDecomposeComponent

abstract class IntervalsSetupSheetDecomposeComponent(
    componentContext: ComponentContext
) : ModalDecomposeComponent(componentContext) {

    abstract fun showRest()

    abstract fun showLongRest()

    abstract fun showWork()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            timerSettingsId: TimerSettingsId,
        ): IntervalsSetupSheetDecomposeComponent
    }
}
