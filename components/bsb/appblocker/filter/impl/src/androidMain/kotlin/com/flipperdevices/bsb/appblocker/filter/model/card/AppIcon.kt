package com.flipperdevices.bsb.appblocker.filter.model.card

import org.jetbrains.compose.resources.DrawableResource

sealed interface AppIcon {
    data class Category(
        val icon: DrawableResource
    ) : AppIcon

    data class App(
        val packageName: String
    ) : AppIcon
}
