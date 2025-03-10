package com.flipperdevices.bsbwearable.card.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.wear.messenger.model.TimerActionMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.bsbwearable.card.composable.WearScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val wearMessageProducer: WearMessageProducer,
    private val cardStorageApi: CardStorageApi
) : CardDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<TimerSettings?> {
        return cardStorageApi.settingFlow
    }

    private fun getBlockerState(): StateFlow<BlockedAppCount?> {
        return  cardStorageApi.appBlockerFlow
    }
    private val scope = coroutineScope()

    @Composable
    override fun Render(modifier: Modifier) {
        WearScreenComposable(
            settings = getTimerState().collectAsState().value,
            blockerState = getBlockerState().collectAsState().value,
            onStartClick = {
                scope.launch { wearMessageProducer.produce(TimerActionMessage.Restart) }
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
