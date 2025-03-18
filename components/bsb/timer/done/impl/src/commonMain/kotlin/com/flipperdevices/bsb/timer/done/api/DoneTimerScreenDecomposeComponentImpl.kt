package com.flipperdevices.bsb.timer.done.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.analytics.metric.api.model.TimerConfigSnapshot
import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsb.timer.done.composable.DoneComposableContent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@Suppress("LongParameterList")
class DoneTimerScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onFinishCallback: OnFinishCallback,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerApi: TimerApi,
    private val krateApi: KrateApi,
    private val metricApi: MetricApi,
    private val appBlockerFilterApi: AppBlockerFilterApi,
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

    init {
        coroutineScope(FlipperDispatchers.default).launch {
            timerApi.getTimestampState().value.runningOrNull?.settings?.let { settings ->
                val blockedAppCountAsync = async { appBlockerFilterApi.getBlockedAppCount().first() }
                val blockedCategoriesAsync = async { appBlockerFilterApi.getBlockedCategories() }
                metricApi.reportEvent(
                    BEvent.TimerCompleted(
                        TimerConfigSnapshot(
                            isIntervalsEnabled = settings.intervalsSettings.isEnabled,
                            totalTimeMillis = settings.totalTime.inWholeMilliseconds,
                            workTimerMillis = settings.intervalsSettings.work.inWholeMilliseconds,
                            restTimeMillis = settings.intervalsSettings.rest.inWholeMilliseconds,
                            isBlockingEnabled = when (blockedAppCountAsync.await()) {
                                is BlockedAppCount.Count,
                                BlockedAppCount.All -> true

                                else -> false
                            },
                            blockingCategories = blockedCategoriesAsync.await()
                        )
                    )
                )
            }
        }
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
