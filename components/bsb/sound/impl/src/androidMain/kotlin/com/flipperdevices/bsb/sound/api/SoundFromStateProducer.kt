package com.flipperdevices.bsb.sound.api

import android.content.Context
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

@Inject
@SingleIn(AppGraph::class)
class SoundFromStateProducer(
    private val soundPlayHelper: SoundPlayHelper

) {
    private var lastSoundEvent: Pair<Int, TimerSettings>? = null
    private val mutex = Mutex()

    suspend fun tryPlaySound(state: ControlledTimerState.InProgress) = mutex.withLock {
        if (!state.timerSettings.intervalsSettings.isEnabled
            || !state.timerSettings.soundSettings.alertWhenIntervalEnds
        ) {
            return@withLock
        }
        when (state) {
            is ControlledTimerState.InProgress.Await -> {
                if (Clock.System.now() - state.pausedAt > 1.seconds) {
                    return@withLock
                }
                val soundEvent = state.currentIteration to state.timerSettings
                if (lastSoundEvent != soundEvent) {
                    val sound = when (state.type) {
                        ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> Sound.WORK_FINISHED
                        ControlledTimerState.InProgress.AwaitType.AFTER_REST -> Sound.REST_FINISHED
                    }
                    soundPlayHelper.play(sound)
                    lastSoundEvent = soundEvent
                }
            }

            is ControlledTimerState.InProgress.Running -> {
                if (state.timeLeft > 4.seconds || state.timeLeft < 2.seconds) {
                    return@withLock
                }
                val soundEvent = state.currentIteration to state.timerSettings
                if (lastSoundEvent != soundEvent) {
                    val sound = when (state) {
                        is ControlledTimerState.InProgress.Running.LongRest,
                        is ControlledTimerState.InProgress.Running.Rest -> Sound.REST_COUNTDOWN

                        is ControlledTimerState.InProgress.Running.Work -> Sound.WORK_COUNTDOWN
                    }
                    soundPlayHelper.play(sound)
                    lastSoundEvent = soundEvent
                }
            }
        }
    }

    suspend fun clear() = mutex.withLock {
        lastSoundEvent = null
    }


}