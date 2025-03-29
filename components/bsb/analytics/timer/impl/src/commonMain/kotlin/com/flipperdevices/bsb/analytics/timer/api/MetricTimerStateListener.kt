package com.flipperdevices.bsb.analytics.timer.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class MetricTimerStateListener(
    @Assisted private val timerApi: TimerApi,
    private val scope: CoroutineScope,
    private val metricHandler: MetricHandlerImpl
) : TimerStateListener {
    private var timerStateListenerJob: Job? = null

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        super.onTimerStart(timerSettings)

        metricHandler.onStartTimer(timerSettings)

        timerStateListenerJob?.cancel()
        timerStateListenerJob = scope.launch {
            var prevTimestamp: TimerTimestamp? = null
            combine(
                timerApi.getTimestampState(),
                timerApi.getState()
            ) { timestampState, state ->
                metricHandler.onTimerStateChanged(state, prevTimestamp, timestampState)
                prevTimestamp = timestampState
            }
        }
    }

    override suspend fun onTimerStop() {
        super.onTimerStop()
        timerStateListenerJob?.cancel()
        timerStateListenerJob = null
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerStateListener.Factory::class, multibinding = true)
    class Factory(
        val factory: (
            timerApi: TimerApi
        ) -> MetricTimerStateListener
    ) : TimerStateListener.Factory {
        override fun invoke(
            timerApi: TimerApi
        ) = factory(timerApi)
    }
}
