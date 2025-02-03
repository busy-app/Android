package com.flipperdevices.bsb.timer.main.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.root.api.LocalRootNavigation
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.main.composable.TimerOffComposableScreen
import com.flipperdevices.bsb.timer.setup.api.TimerSetupScreenDecomposeComponent
import com.flipperdevices.bsb.timer.setup.api.TimerSetupSheetDecomposeComponentImpl
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TimerOffScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    timerSetupDecomposeComponentFactory: TimerSetupScreenDecomposeComponent.Factory,
    private val timerApi: TimerApi,
    private val timerSetupSheetDecomposeComponentFactory: (ComponentContext) -> TimerSetupSheetDecomposeComponentImpl
) : ScreenDecomposeComponent(componentContext), StatusBarIconStyleProvider by iconStyleProvider {
    private val setupDecomposeComponent = timerSetupDecomposeComponentFactory(
        componentContext = childContext("setupTimerDecomposeComponent")
    )

    private val timerSetupSheetDecomposeComponent = timerSetupSheetDecomposeComponentFactory(
        childContext("timerSetupSheetDecomposeComponent")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by setupDecomposeComponent.timerState.collectAsState()
        val rootNavigation = LocalRootNavigation.current

        TimerOffComposableScreen(
            onTagsOpenClick = {
            },
            onTimeClick = { timerSetupSheetDecomposeComponent.show() }
        )
        timerSetupSheetDecomposeComponent.Render(Modifier)
    }
}
