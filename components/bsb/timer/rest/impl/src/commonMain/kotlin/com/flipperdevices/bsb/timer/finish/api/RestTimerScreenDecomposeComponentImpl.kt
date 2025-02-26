package com.flipperdevices.bsb.timer.finish.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.util.pause
import com.flipperdevices.bsb.timer.background.util.resume
import com.flipperdevices.bsb.timer.background.util.skip
import com.flipperdevices.bsb.timer.background.util.stop
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
    private val timerApi: TimerApi,
) : RestTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerApi.getState().collectAsState()
        when (val state = state) {
            is ControlledTimerState.Running -> {
                RestComposableContent(
                    modifier = modifier,
                    onSkip = {
                        timerApi.skip()
                    },
                    timeLeft = state.timeLeft,
                    statusType = when (breakType) {
                        BreakType.SHORT -> StatusType.REST
                        BreakType.LONG -> StatusType.LONG_REST
                    },
                    onBackClick = {
                        timerApi.stop()
                    },
                    onPauseClick = {
                        timerApi.pause()
                    }
                )
                if (state.isOnPause) {
                    PauseFullScreenOverlayComposable(
                        onStartClick = { timerApi.resume() }
                    )
                }
            }

            is ControlledTimerState.Await,
            ControlledTimerState.NotStarted,
            ControlledTimerState.Finished -> Unit
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
