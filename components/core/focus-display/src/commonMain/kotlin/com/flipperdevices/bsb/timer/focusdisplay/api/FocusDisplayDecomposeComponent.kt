package com.flipperdevices.bsb.timer.focusdisplay.api

import com.arkivanov.essenty.lifecycle.Lifecycle

abstract class FocusDisplayDecomposeComponent(
    lifecycle: Lifecycle
) : Lifecycle.Callbacks {

    init {
        lifecycle.subscribe(this)
    }

    fun interface Factory {
        operator fun invoke(
            lifecycle: Lifecycle
        ): FocusDisplayDecomposeComponent
    }
}
