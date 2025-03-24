package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.tc_open_profile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.analytics.metric.api.model.TimerConfigSnapshot
import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.root.api.LocalRootNavigation
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsb.timer.cards.composable.BusyCardComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.setup.api.TimerSetupSheetDecomposeComponent
import com.flipperdevices.core.buildkonfig.BuildKonfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class CardsDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi,
    private val krateApi: KrateApi,
    private val appBlockerFilterApi: AppBlockerFilterApi,
    timerSetupSheetDecomposeComponentFactory: TimerSetupSheetDecomposeComponent.Factory,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val metricApi: MetricApi
) : CardsDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {
    private val timerSetupSheetDecomposeComponent = timerSetupSheetDecomposeComponentFactory(
        componentContext = childContext("timerSetupSheetDecomposeComponent_CardsDecomposeComponentImpl")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        val rootNavigation = LocalRootNavigation.current
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(92.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val blockerState by remember {
                    appBlockerFilterApi.getBlockedAppCount()
                }.collectAsState(BlockedAppCount.TurnOff)
                @Suppress("MagicNumber")
                BusyCardComposable(
                    background = Color(0xFFE50000), // todo no color in design
                    name = "BUSY", // todo raw string
                    settings = krateApi.timerSettingsKrate
                        .stateFlow(coroutineScope)
                        .collectAsState()
                        .value,
                    blockerState = blockerState,
                    onClick = {
                        timerSetupSheetDecomposeComponent.show()
                    }
                )
                ButtonTimerComposable(
                    state = ButtonTimerState.START,
                    onClick = {
                        coroutineScope.launch {
                            timerApi.getTimestampState().value.runningOrNull?.settings?.let { settings ->
                                metricApi.reportEvent(
                                    BEvent.TimerStarted(
                                        TimerConfigSnapshot(
                                            isIntervalsEnabled = settings.intervalsSettings.isEnabled,
                                            totalTimeMillis = settings.totalTime.inWholeMilliseconds,
                                            workTimerMillis = settings.intervalsSettings.work.inWholeMilliseconds,
                                            restTimeMillis = settings.intervalsSettings.rest.inWholeMilliseconds,
                                            isBlockingEnabled = when (blockerState) {
                                                is BlockedAppCount.Count, BlockedAppCount.All -> true
                                                else -> false
                                            },
                                            blockingCategories = appBlockerFilterApi.getBlockedCategories()
                                        )
                                    )
                                )
                            }
                        }
                        coroutineScope.launch {
                            val settings = krateApi.timerSettingsKrate.flow.first()
                            timerApi.startWith(settings)
                        }
                    }
                )
                if (BuildKonfig.IS_TEST_LOGIN_BUTTON_SHOWN) {
                    BChipButton(
                        painter = null,
                        text = stringResource(Res.string.tc_open_profile),
                        contentColor = LocalCorruptedPallet.current
                            .black
                            .invert,
                        background = LocalCorruptedPallet.current
                            .white
                            .invert,
                        contentPadding = PaddingValues(
                            horizontal = 48.dp,
                            vertical = 24.dp
                        ),
                        onClick = {
                            rootNavigation.push(RootNavigationConfig.Profile(null))
                        },
                    )
                }
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
