package com.flipperdevices.bsb.timer.done.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.controller.TimerControllerApi
import com.flipperdevices.bsb.timer.done.composable.DoneComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class DoneTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onFinishCallback: OnFinishCallback,
    @Assisted private val timerSettings: TimerSettings,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerControllerApi: TimerControllerApi
) : DoneTimerScreenDecomposeComponent(componentContext),
    StatusBarIconStyleProvider by iconStyleProvider {

    @Composable
    override fun Render(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        DoneComposableContent(
            onFinishClick = {
                onFinishCallback.invoke()
            },
            onRestartClick = {
                coroutineScope.launch {
                    timerControllerApi.startWith(timerSettings)
                }
            }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, DoneTimerScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onFinishCallback: OnFinishCallback,
            timerSettings: TimerSettings
        ) -> DoneTimerScreenDecomposeComponentImpl
    ) : DoneTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onFinishCallback: OnFinishCallback,
            timerSettings: TimerSettings
        ) = factory(componentContext, onFinishCallback, timerSettings)
    }
}
