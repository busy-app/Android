package com.flipperdevices.bsb.timer.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerService
import com.flipperdevices.bsb.timer.background.model.TimerServiceState
import com.flipperdevices.bsb.timer.common.composable.appbar.PauseFullScreenOverlayComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.StatusType
import com.flipperdevices.bsb.timer.finish.composable.RestComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class RestTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val breakType: BreakType,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerService: TimerService,
) : RestTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    @Composable
    override fun Render(modifier: Modifier) {
        val state = timerService.state.collectAsState()
        when (val state = state.value) {
            TimerServiceState.Pending, TimerServiceState.Finished -> Unit
            is TimerServiceState.Started -> {
                RestComposableContent(
                    modifier = modifier,
                    onSkip = { timerService.skip() },
                    timeLeft = state.timerState.timerState.duration,
                    statusType = when (breakType) {
                        BreakType.SHORT -> StatusType.REST
                        BreakType.LONG -> StatusType.LONG_REST
                    },
                    onBackClick = {
                        timerService.stop()
                    },
                    onPauseClick = {
                        timerService.togglePause()
                    }
                )
                if (state.timerState.isOnPause) {
                    PauseFullScreenOverlayComposable(
                        onStartClick = { timerService.togglePause() }
                    )
                }
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, RestTimerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            breakType: BreakType
        ) -> RestTimerScreenDecomposeComponentImpl
    ) : RestTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            breakType: BreakType
        ) = factory(componentContext, breakType)
    }
}
