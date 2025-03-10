package com.flipperdevices.bsbwearable.card.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsbwearable.card.composable.WearScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
    private val cardStorageApi: CardStorageApi
) : CardDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<TimerSettings?> {
        return cardStorageApi.settingFlow
    }

    private fun getBlockerState(): StateFlow<BlockedAppCount?> {
        return cardStorageApi.appBlockerFlow
    }

    @Composable
    override fun Render(modifier: Modifier) {
        WearScreenComposable(
            settings = getTimerState().collectAsState().value,
            blockerState = getBlockerState().collectAsState().value,
            onStartClick = onStartClick@{
                val settings = getTimerState().value ?: return@onStartClick
                timerApi.startWith(settings)
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
