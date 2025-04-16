package com.flipperdevices.bsb.timer.controller

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.throttleFirst
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.trustedclock.TrustedClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesBinding(AppGraph::class, TimerControllerApi::class)
@SingleIn(AppGraph::class)
class TimerControllerApiImpl(
    private val scope: CoroutineScope,
    private val timerApi: TimerApi,
    private val trustedClock: TrustedClock
) : TimerControllerApi, LogTagProvider {
    override val TAG: String = "TimerControllerApi"

    override fun updateState(block: (TimerTimestamp) -> TimerTimestamp) {
        val newState = block.invoke(timerApi.getTimestampState().value)
        timerApi.setTimestampState(newState)
    }

    override fun pause() {
        updateState { state ->
            when (state) {
                is TimerTimestamp.Pending -> state
                is TimerTimestamp.Running -> {
                    if (state.pause == null) {
                        state.copy(
                            pause = trustedClock.now(),
                            lastSync = trustedClock.now()
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    override fun confirmNextStep() {
        val awaitState =
            timerApi.getState().value as? ControlledTimerState.InProgress.Await ?: return
        updateState { state ->
            when (state) {
                is TimerTimestamp.Pending -> state
                is TimerTimestamp.Running -> {
                    state.copy(
                        confirmNextStepClick = trustedClock.now().plus(1.seconds),
                        start = state.start.plus(trustedClock.now().minus(awaitState.pausedAt)),
                        lastSync = trustedClock.now()
                    )
                }
            }
        }
    }

    override fun resume() {
        updateState { state ->
            when (state) {
                is TimerTimestamp.Pending -> state
                is TimerTimestamp.Running -> {
                    val pause = state.pause
                    if (pause != null) {
                        val diff = trustedClock.now() - pause
                        state.copy(
                            pause = null,
                            start = state.start.plus(diff),
                            confirmNextStepClick = state.confirmNextStepClick.plus(diff),
                            lastSync = trustedClock.now()
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    override fun stop() {
        updateState {
            TimerTimestamp.Pending.Finished(trustedClock.now())
        }
    }

    /**
     * Fix last milliseconds of current Instant
     */
    private fun Instant.truncate(): Instant {
        val millis = toEpochMilliseconds()
        val diff = MILLISECONDS_IN_SECOND - (millis % MILLISECONDS_IN_SECOND)
        val instant = this + diff.milliseconds
        return instant
    }

    private val skipThrottle = throttleFirst<Unit>(scope) {
        updateState { state ->
            when (state) {
                is TimerTimestamp.Pending -> state
                is TimerTimestamp.Running -> {
                    val startedState =
                        timerApi.getState().value as? ControlledTimerState.InProgress.Running
                    startedState ?: return@updateState state
                    when (val localTimeLeft = startedState.timeLeft) {
                        is TimerDuration.Finite -> {
                            state.copy(
                                start = state.start.minus(localTimeLeft.instance).truncate(),
                                lastSync = trustedClock.now()
                            )
                        }

                        TimerDuration.Infinite -> {
                            state
                        }
                    }
                }
            }
        }
    }

    override fun skip() {
        skipThrottle.invoke(Unit)
    }

    override fun startWith(settings: TimerSettings) {
        timerApi.setTimestampState(
            state = TimerTimestamp.Running(
                settings = settings,
                lastSync = trustedClock.now(),
                start = trustedClock.now(),
                noOffsetStart = trustedClock.now()
            ),
        )
    }

    companion object {
        private val MILLISECONDS_IN_SECOND = 1.seconds.inWholeMilliseconds
    }
}
