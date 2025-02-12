package com.flipperdevices.bsb.timer.main.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.main.composable.TimerOffComposableScreen
import com.flipperdevices.bsb.timer.setup.api.TimerSetupSheetDecomposeComponent
import com.flipperdevices.core.data.timer.TimerState
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TimerOffScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    private val krateApi: KrateApi,
    timerSetupSheetDecomposeComponentFactory: TimerSetupSheetDecomposeComponent.Factory
) : ScreenDecomposeComponent(componentContext), StatusBarIconStyleProvider by iconStyleProvider {
    private val timerSetupSheetDecomposeComponent = timerSetupSheetDecomposeComponentFactory(
        componentContext = childContext("timerSetupSheetDecomposeComponent")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        TimerOffComposableScreen(
            onTimeClick = { timerSetupSheetDecomposeComponent.show() },
            onStartClick = {
                coroutineScope.launch {
                    val timerState = TimerState(krateApi.timerSettingsKrate.flow.first().totalTime)
                    timerApi.startTimer(timerState)
                }
            }
        )
        timerSetupSheetDecomposeComponent.Render(Modifier)
    }
}
