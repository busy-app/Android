package com.flipperdevices.bsb.sound.api

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.sound.helper.Sound
import com.flipperdevices.bsb.sound.helper.SoundPlayHelper
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.flipperdevices.core.vibrator.api.BVibratorApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

data class SoundEventSnapshot(
    val durationSec: Long,
    val iteration: Int,
    val timerSettings: TimerSettings,
    val sound: Sound
)

@Inject
@SingleIn(AppGraph::class)
class SoundFromStateProducer(
    private val soundPlayHelper: SoundPlayHelper,
    private val vibratorApi: BVibratorApi
) : LogTagProvider {
    override val TAG = "SoundFromStateProducer"

    private var lastSoundEvent: SoundEventSnapshot? = null
    private val mutex = Mutex()

    suspend fun tryPlaySound(state: ControlledTimerState.InProgress.Running) = mutex.withLock {
        if (!state.timerSettings.intervalsSettings.isEnabled ||
            !state.timerSettings.soundSettings.alertWhenIntervalEnds ||
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

        val sound = when (state) {
            is ControlledTimerState.InProgress.Running.LongRest,
            is ControlledTimerState.InProgress.Running.Rest -> if (timeLeft < 1.seconds) {
                Sound.REST_FINISHED
            } else {
                Sound.REST_COUNTDOWN
            }

            is ControlledTimerState.InProgress.Running.Work -> if (timeLeft < 1.seconds) {
                Sound.WORK_FINISHED
            } else {
                Sound.WORK_COUNTDOWN
            }
        }

        val soundEvent = SoundEventSnapshot(
            durationSec = timeLeft.inWholeSeconds,
            state.currentIteration,
            state.timerSettings,
            sound
        )
        if (lastSoundEvent != soundEvent) {
            when (sound) {
                Sound.REST_COUNTDOWN,
                Sound.WORK_COUNTDOWN -> {
                }

                Sound.REST_FINISHED,
                Sound.WORK_FINISHED -> vibratorApi.vibrateOnce(500.milliseconds)
            }
            soundPlayHelper.play(sound)
            lastSoundEvent = soundEvent
        } else {
            info { "Skip, because ${state.currentIteration to state.timerSettings} already exist" }
        }
    }

    suspend fun clear() = mutex.withLock {
        lastSoundEvent = null
    }
}
