package com.flipperdevices.bsb.timer.vibration.api

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.vibrator.api.BVibratorApi
import com.flipperdevices.core.vibrator.api.VibrateMode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

data class VibrationEventSnapshot(
    val durationSec: Long,
    val iteration: Int,
    val timerSettings: TimerSettings
)

@Inject
@SingleIn(AppGraph::class)
class VibrationFromStateProducer(
    private val vibrationApi: BVibratorApi
) : LogTagProvider {
    override val TAG = "VibrationFromStateProducer"


    private var lastVibrationEvent: VibrationEventSnapshot? = null
    private val mutex = Mutex()

    suspend fun tryVibrate(state: ControlledTimerState.InProgress.Running) = mutex.withLock {
        if (!state.timerSettings.intervalsSettings.isEnabled ||
            state.isOnPause
        ) {
            return@withLock
        }
        val timeLeft = when (val localTimeLeft = state.timeLeft) {
            is TimerDuration.Finite -> localTimeLeft.instance
            TimerDuration.Infinite -> return@withLock
        }

        if (timeLeft >= 4.seconds) {
            return@withLock
        }

        val vibrationEvent = VibrationEventSnapshot(
            durationSec = timeLeft.inWholeSeconds,
            state.currentIteration,
            state.timerSettings
        )
        if (lastVibrationEvent != vibrationEvent) {
            vibrationApi.vibrateOnce(VibrateMode.TICK)
            lastVibrationEvent = vibrationEvent
        }
    }

    suspend fun clear() = mutex.withLock {
        lastVibrationEvent = null
    }
}
