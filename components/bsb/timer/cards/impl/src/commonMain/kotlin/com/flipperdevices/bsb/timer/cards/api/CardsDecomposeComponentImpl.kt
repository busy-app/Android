package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.cards.composable.OldCardsScreenComposable
import com.flipperdevices.bsb.timer.cards.viewmodel.CardsViewModel
import com.flipperdevices.bsb.timer.controller.TimerControllerApi
import com.flipperdevices.bsb.timer.setup.api.CardEditSheetDecomposeComponent
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

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
        OldCardsScreenComposable(
            cardsViewModel = cardsViewModel,
            timerSetupSheetDecomposeComponent = timerSetupSheetDecomposeComponent,
            timerControllerApi = timerControllerApi,
            modifier = modifier
        )
    }

//    @Inject
//    @ContributesBinding(AppGraph::class, CardsDecomposeComponent.Factory::class)
//    class Factory(
//        private val factory: (
//            componentContext: ComponentContext
//        ) -> CardsDecomposeComponentImpl
//    ) : CardsDecomposeComponent.Factory {
//        override fun invoke(
//            componentContext: ComponentContext
//        ) = factory(componentContext)
//    }
}
