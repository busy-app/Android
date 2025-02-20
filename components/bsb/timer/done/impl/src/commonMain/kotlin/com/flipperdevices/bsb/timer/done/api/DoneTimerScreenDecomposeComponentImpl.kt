package com.flipperdevices.bsb.timer.done.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsb.timer.done.composable.DoneComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class DoneTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onFinishCallback: OnFinishCallback,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    private val krateApi: KrateApi,
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
                    val settings = krateApi.timerSettingsKrate.flow.first()
                    timerApi.startWith(settings)
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
        ) -> DoneTimerScreenDecomposeComponentImpl
    ) : DoneTimerScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onFinishCallback: OnFinishCallback,
        ) = factory(componentContext, onFinishCallback)
    }
}
