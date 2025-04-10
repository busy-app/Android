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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.tc_open_profile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.root.api.LocalRootNavigation
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import com.flipperdevices.bsb.timer.cards.composable.BusyCardComposable
import com.flipperdevices.bsb.timer.cards.viewmodel.CardsViewModel
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.bsb.timer.controller.TimerControllerApi
import com.flipperdevices.bsb.timer.setup.api.CardEditSheetDecomposeComponent
import com.flipperdevices.core.buildkonfig.BuildKonfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class CardsDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val cardsViewModelFactory: KIProvider<CardsViewModel>,
    cardEditSheetDecomposeComponentFactory: CardEditSheetDecomposeComponent.Factory,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerControllerApi: TimerControllerApi
) : CardsDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {
    private val timerSetupSheetDecomposeComponent = cardEditSheetDecomposeComponentFactory(
        componentContext = childContext("timerSetupSheetDecomposeComponent_CardsDecomposeComponentImpl")
    )

    private val cardsViewModel = viewModelWithFactory(null) {
        cardsViewModelFactory()
    }

    @Composable
    @Suppress("LongMethod")
    override fun Render(modifier: Modifier) {
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
                val cards by cardsViewModel.getTimerSettingsState().collectAsState()

                cards.forEach { card ->
                    BusyCardComposable(
                        background = Color(color = 0xFFE50000),
                        settings = card.settings,
                        blockerState = card.blockedAppCount,
                        onClick = {
                            timerSetupSheetDecomposeComponent.show(card.settings.id)
                        }
                    )
                }

                ButtonTimerComposable(
                    state = ButtonTimerState.START,
                    onClick = {
                        cards.firstOrNull()?.let {
                            timerControllerApi.startWith(it.settings)
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
