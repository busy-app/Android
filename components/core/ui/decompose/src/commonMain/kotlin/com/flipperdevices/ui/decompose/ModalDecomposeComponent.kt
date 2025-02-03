package com.flipperdevices.ui.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.flipperdevices.ui.decompose.internal.WindowDecorator
import com.flipperdevices.ui.decompose.internal.createWindowDecorator
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider

abstract class ModalDecomposeComponent(
    componentContext: ComponentContext
) : DecomposeComponent(),
    ComponentContext by componentContext,
    Lifecycle.Callbacks {
    init {
        lifecycle.subscribe(this)
    }
}
