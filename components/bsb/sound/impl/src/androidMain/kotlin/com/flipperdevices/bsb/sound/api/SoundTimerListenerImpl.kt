package com.flipperdevices.bsb.sound.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class SoundTimerListenerImpl(
    private val timerApi: TimerApi,
    private val scope: CoroutineScope,
    private val soundFromStateProducer: SoundFromStateProducer
) : TimerStateListener {
    private var timerStateListenerJob: Job? = null

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        timerStateListenerJob?.cancel()
        if (!timerSettings.soundSettings.alertWhenIntervalEnds) {
            return
        }
        runBlocking { soundFromStateProducer.clear() }
        timerStateListenerJob = timerApi.getState().onEach { internalState ->
            return@onEach when (internalState) {
                ControlledTimerState.Finished,
                ControlledTimerState.NotStarted,
                is ControlledTimerState.InProgress.Await -> return@onEach

                is ControlledTimerState.InProgress.Running -> {
                    soundFromStateProducer.tryPlaySound(internalState)
                }
            }
        }.launchIn(scope)
    }

    override suspend fun onTimerStop() {
        timerStateListenerJob?.cancel()
        runBlocking { soundFromStateProducer.clear() }
    }
}
