package com.flipperdevices.bsbwearable.root.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsbwearable.root.api.model.RootNavigationConfig
import com.flipperdevices.ui.decompose.CompositeDecomposeComponent

abstract class RootDecomposeComponent : CompositeDecomposeComponent<RootNavigationConfig>() {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootDecomposeComponent
    }
}
