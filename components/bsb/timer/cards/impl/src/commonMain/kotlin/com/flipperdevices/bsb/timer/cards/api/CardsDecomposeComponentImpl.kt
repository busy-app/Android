package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.cards.composable.BusyCardComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.setup.api.TimerSetupSheetDecomposeComponent
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardsDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
    private val krateApi: KrateApi,
    timerSetupSheetDecomposeComponentFactory: TimerSetupSheetDecomposeComponent.Factory
) : CardsDecomposeComponent(componentContext) {
    private val timerSetupSheetDecomposeComponent = timerSetupSheetDecomposeComponentFactory(
        componentContext = childContext("timerSetupSheetDecomposeComponent_CardsDecomposeComponentImpl")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(92.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                @Suppress("MagicNumber")
                BusyCardComposable(
                    background = Color(0xFFE50000), // todo no color in design
                    name = "BUSY",
                    settings = krateApi.timerSettingsKrate
                        .stateFlow(coroutineScope)
                        .collectAsState()
                        .value,
                    onClick = {
                        timerSetupSheetDecomposeComponent.show()
                    }
                )
                ButtonTimerComposable(
                    state = ButtonTimerState.START,
                    onClick = {
                        coroutineScope.launch {
                            val totalTime = krateApi.timerSettingsKrate.flow.first().totalTime
                            timerApi.startTimer(TimerState(totalTime))
                        }
                    }
                )
            }
        }
        timerSetupSheetDecomposeComponent.Render(Modifier)
    }

    @Inject
    @ContributesBinding(AppGraph::class, CardsDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> CardsDecomposeComponentImpl
    ) : CardsDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
