package com.flipperdevices.bsbwearable.card.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsbwearable.card.composable.WearScreenComposable
import com.flipperdevices.bsbwearable.card.viewmodel.data.CardStorageApi
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
    private val cardStorageApi: CardStorageApi
) : CardDecomposeComponent(componentContext) {

    @Composable
    override fun Render(modifier: Modifier) {
        val cards by cardStorageApi.settingFlow.collectAsState()

        WearScreenComposable(
            settingsList = cards,
            onStartClick = onStartClick@{
                val card = cards.firstOrNull() ?: return@onStartClick
                timerApi.startWith(card.instance)
            }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, CardDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> CardDecomposeComponentImpl
    ) : CardDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
