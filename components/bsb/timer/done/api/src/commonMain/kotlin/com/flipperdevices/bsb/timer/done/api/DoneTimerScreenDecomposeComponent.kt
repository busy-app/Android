package com.flipperdevices.bsb.timer.done.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent

abstract class DoneTimerScreenDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onFinishCallback: OnFinishCallback,
            timerSettings: TimerSettings,
        ): DoneTimerScreenDecomposeComponent
    }

    fun interface OnFinishCallback {
        operator fun invoke()
    }
}
