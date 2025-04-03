package com.flipperdevices.bsb.analytics.timer.api

import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.analytics.metric.api.model.TimerConfigSnapshot
import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration
import kotlin.time.DurationUnit

@Inject
@SingleIn(AppGraph::class)
class MetricHandlerImpl(
    private val metricApi: MetricApi,
    private val cardAppBlockerApi: CardAppBlockerApi
) {
    suspend fun onStartTimer(timerSettings: TimerSettings) {
        val blockedAppDetailedState = cardAppBlockerApi
            .getBlockedAppDetailedState(cardId = timerSettings.id)
            .first()

        metricApi.reportEvent(
            BEvent.TimerStarted(
                TimerConfigSnapshot(
                    isIntervalsEnabled = timerSettings.intervalsSettings.isEnabled,
                    totalTimeMillis = when (val localTotalTime = timerSettings.totalTime) {
                        is TimerDuration.Finite -> localTotalTime.instance.inWholeMilliseconds
                        TimerDuration.Infinite -> -1
                    },
                    workTimerMillis = timerSettings.intervalsSettings.work.inWholeMilliseconds,
                    restTimeMillis = timerSettings.intervalsSettings.rest.inWholeMilliseconds,
                    isBlockingEnabled = blockedAppDetailedState.isBlockingEnabled(),
                    blockingCategories = blockedAppDetailedState.getBlockedCategories()
                )
            )
        )
    }

    private suspend fun onFinish(settings: TimerSettings) {
        val blockedAppDetailedState = cardAppBlockerApi
            .getBlockedAppDetailedState(cardId = settings.id)
            .first()
        metricApi.reportEvent(
            BEvent.TimerCompleted(
                TimerConfigSnapshot(
                    isIntervalsEnabled = settings.intervalsSettings.isEnabled,
                    totalTimeMillis = when (val localTotalTime = settings.totalTime) {
                        is TimerDuration.Finite -> localTotalTime.instance.inWholeMilliseconds
                        TimerDuration.Infinite -> -1
                    },
                    workTimerMillis = settings.intervalsSettings.work.inWholeMilliseconds,
                    restTimeMillis = settings.intervalsSettings.rest.inWholeMilliseconds,
                    isBlockingEnabled = blockedAppDetailedState.isBlockingEnabled(),
                    blockingCategories = blockedAppDetailedState.getBlockedCategories()
                )
            )
        )
    }

    private suspend fun onResume(
        prevTimerTimestamp: TimerTimestamp.Running
    ) {
        val pauseOn = prevTimerTimestamp.pause ?: return
        val pauseDurationMillis = Clock.System.now() - pauseOn

        metricApi.reportEvent(
            BEvent.TimerResumed(
                pauseDurationMillis = pauseDurationMillis.toLong(DurationUnit.MILLISECONDS)
            )
        )
    }

    private suspend fun onPause(timerTimestamp: TimerTimestamp.Running) {
        val pause = timerTimestamp.pause ?: return

        metricApi.reportEvent(
            BEvent.TimerPaused(
                timePassedMillis = (pause - timerTimestamp.noOffsetStart)
                    .toLong(DurationUnit.MILLISECONDS)
            )
        )
    }

    @Suppress("NestedBlockDepth")
    suspend fun onTimerStateChanged(
        state: ControlledTimerState,
        prevTimerTimestamp: TimerTimestamp?,
        timerTimestamp: TimerTimestamp,
    ) {
        when (state) {
            is ControlledTimerState.Finished -> onFinish(state.timerSettings)
            is ControlledTimerState.InProgress.Running -> {
                if (timerTimestamp is TimerTimestamp.Running && prevTimerTimestamp is TimerTimestamp.Running) {
                    if (timerTimestamp.pause != prevTimerTimestamp.pause) {
                        if (timerTimestamp.pause != null) {
                            onPause(timerTimestamp)
                        } else {
                            onResume(prevTimerTimestamp)
                        }
                    }
                }
            }

            is ControlledTimerState.InProgress.Await,
            ControlledTimerState.NotStarted -> {
            }
        }
    }
}

private fun BlockedAppDetailedState.isBlockingEnabled() = when (this) {
    BlockedAppDetailedState.All,
    is BlockedAppDetailedState.TurnOnWhitelist -> true

    BlockedAppDetailedState.TurnOff -> false
}

private fun BlockedAppDetailedState.getBlockedCategories() = when (this) {
    BlockedAppDetailedState.All -> listOf("All")
    BlockedAppDetailedState.TurnOff -> emptyList()
    is BlockedAppDetailedState.TurnOnWhitelist ->
        entities
            .filterIsInstance<BlockedAppEntity.Category>()
            .map { it.categoryId.toString() }
}
