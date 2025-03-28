package com.flipperdevices.bsb.analytics.timer.api

import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.analytics.metric.api.model.TimerConfigSnapshot
import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class MetricTimerStateListener(
    private val timerApi: TimerApi,
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
}