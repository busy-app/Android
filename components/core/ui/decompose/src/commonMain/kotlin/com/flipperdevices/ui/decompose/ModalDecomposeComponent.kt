package com.flipperdevices.ui.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ModalDecomposeComponent(
    componentContext: ComponentContext
) : DecomposeComponent(),
    ComponentContext by componentContext,
    Lifecycle.Callbacks {
    init {
        lifecycle.subscribe(this)
    }
}
