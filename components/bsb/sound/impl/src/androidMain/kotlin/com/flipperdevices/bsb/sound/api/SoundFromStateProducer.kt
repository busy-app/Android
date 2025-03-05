package com.flipperdevices.bsb.sound.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.sound.helper.Sound
import com.flipperdevices.bsb.sound.helper.SoundPlayHelper
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.info
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

data class SoundEventSnapshot(
    val iteration: Int,
    val timerSettings: TimerSettings,
    val sound: Sound
)

@Inject
@SingleIn(AppGraph::class)
class SoundFromStateProducer(
    private val soundPlayHelper: SoundPlayHelper

) {
    private var lastSoundEvent: SoundEventSnapshot? = null
    private val mutex = Mutex()

    suspend fun tryPlaySound(state: ControlledTimerState.InProgress) = mutex.withLock {
        if (!state.timerSettings.intervalsSettings.isEnabled ||
            !state.timerSettings.soundSettings.alertWhenIntervalEnds
        ) {
            return@withLock
        }
        when (state) {
            is ControlledTimerState.InProgress.Await -> {
                if (Clock.System.now() - state.pausedAt > 1.seconds) {
                    return@withLock
                }
                val sound = when (state.type) {
                    ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> Sound.WORK_FINISHED
                    ControlledTimerState.InProgress.AwaitType.AFTER_REST -> Sound.REST_FINISHED
                }
                val soundEvent = SoundEventSnapshot(
                    state.currentIteration,
                    state.timerSettings,
                    sound
                )
                if (lastSoundEvent != soundEvent) {
                    soundPlayHelper.play(sound)
                    lastSoundEvent = soundEvent
                }
            }

            is ControlledTimerState.InProgress.Running -> {
                if (state.timeLeft > 4.seconds || state.timeLeft < 2.seconds) {
                    return@withLock
                }
                val sound = when (state) {
                    is ControlledTimerState.InProgress.Running.LongRest,
                    is ControlledTimerState.InProgress.Running.Rest -> Sound.REST_COUNTDOWN

                    is ControlledTimerState.InProgress.Running.Work -> Sound.WORK_COUNTDOWN
                }
                val soundEvent = SoundEventSnapshot(
                    state.currentIteration,
                    state.timerSettings,
                    sound
                )
                if (lastSoundEvent != soundEvent) {
                    soundPlayHelper.play(sound)
                    lastSoundEvent = soundEvent
                } else {
                    info { "Skip, because ${state.currentIteration to state.timerSettings} already exist" }
                }
            }
        }
    }

    suspend fun clear() = mutex.withLock {
        lastSoundEvent = null
    }
}
