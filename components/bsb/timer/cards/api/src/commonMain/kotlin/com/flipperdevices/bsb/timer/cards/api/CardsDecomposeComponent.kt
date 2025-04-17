package com.flipperdevices.bsb.timer.cards.api

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.ui.decompose.CompositeDecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import kotlinx.serialization.Serializable

abstract class CardsDecomposeComponent() : CompositeDecomposeComponent<CardsNavigationConfig>() {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): CardsDecomposeComponent
    }
}

@Serializable
data class CardRenameItem(
    val videoUri: String,
    val name: String,
    val id: TimerSettingsId
)

@Serializable
sealed interface CardsNavigationConfig {
    @Serializable
    data object Pager : CardsNavigationConfig

    @Serializable
    data class Rename(val cardRenameItem: CardRenameItem) : CardsNavigationConfig

    @Serializable
    data object Settings : CardsNavigationConfig
}