package com.flipperdevices.bsb.timer.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.finish.composable.FinishComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class FinishTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val breakType: BreakType
) : FinishTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    @Composable
    override fun Render(modifier: Modifier) {
        FinishComposableContent(
            modifier = modifier,
            breakType = breakType,
            doneRange = 1..4,
            onDoneClick = {},
            onRestClick = {}
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, FinishTimerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> FinishTimerScreenDecomposeComponentImpl
    ) : FinishTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
