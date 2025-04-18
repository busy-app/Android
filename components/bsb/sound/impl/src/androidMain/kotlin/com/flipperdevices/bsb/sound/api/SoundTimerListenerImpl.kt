package com.flipperdevices.bsb.sound.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class SoundTimerListenerImpl(
    @Assisted private val timerApi: TimerApi,
    private val scope: CoroutineScope,
    private val soundFromStateProducer: SoundFromStateProducer
) : TimerStateListener {
    private var timerStateListenerJob: Job? = null

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        timerStateListenerJob?.cancel()
        if (!timerSettings.soundSettings.alertWhenIntervalEnds) {
            return
        }
        soundFromStateProducer.clear()
        timerStateListenerJob = scope.launch {
            timerApi.getState().collectLatest { internalState ->
                return@collectLatest when (internalState) {
                    is ControlledTimerState.Finished,
                    ControlledTimerState.NotStarted,
                    is ControlledTimerState.InProgress.Await -> return@collectLatest

                    is ControlledTimerState.InProgress.Running -> {
                        soundFromStateProducer.tryPlaySound(internalState)
                    }
                }
            }
        }
    }

    override suspend fun onTimerStop() {
        timerStateListenerJob?.cancel()
        soundFromStateProducer.clear()
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerStateListener.Factory::class, multibinding = true)
    class Factory(
        val factory: (
            timerApi: TimerApi
        ) -> SoundTimerListenerImpl
    ) : TimerStateListener.Factory {
        override fun invoke(
            timerApi: TimerApi
        ) = factory(timerApi)
    }
}
