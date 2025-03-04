package com.flipperdevices.bsbwearable.card.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsbwearable.card.composable.WearScreenComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
) : CardDecomposeComponent(componentContext) {

    // todo
    private fun getTimerState(): StateFlow<TimerSettings> {
        return MutableStateFlow(TimerSettings()).asStateFlow()
    }

    private fun getBlockerState(): StateFlow<BlockedAppCount> {
        return MutableStateFlow(BlockedAppCount.All).asStateFlow()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        WearScreenComposable(
            settings = getTimerState().collectAsState().value,
            blockerState = getBlockerState().collectAsState().value,
            onStartClick = {}
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
