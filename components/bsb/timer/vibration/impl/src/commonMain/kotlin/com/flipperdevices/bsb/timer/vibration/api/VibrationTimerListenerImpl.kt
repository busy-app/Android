package com.flipperdevices.bsb.timer.vibration.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.flipperdevices.core.vibrator.api.BVibratorApi
import com.flipperdevices.core.vibrator.api.VibrateMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject

class VibrationTimerListenerImpl(
    @Assisted private val timerApi: TimerApi,
    private val scope: CoroutineScope,
    private val vibrationFromStateProducer: VibrationFromStateProducer,
    private val vibratorApi: BVibratorApi
) : TimerStateListener, LogTagProvider {
    override val TAG = "VibrationTimerListenerImpl"

    private var timerStateListenerJob: Job? = null
    private val awaitVibrationJobMutex = Mutex()
    private var awaitVibrationJob: Job? = null

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        info { "#onTimerStart" }
        timerStateListenerJob?.cancel()
        vibrationFromStateProducer.clear()
        timerStateListenerJob = scope.launch {
            timerApi.getState().collectLatest { internalState ->
                if (internalState is ControlledTimerState.InProgress.Await) {
                    onAwaitState()
                } else {
                    awaitVibrationJobMutex.withLock {
                        awaitVibrationJob?.cancel()
                        awaitVibrationJob = null
                    }
                }
                when (internalState) {
                    is ControlledTimerState.Finished,
                    ControlledTimerState.NotStarted,
                    is ControlledTimerState.InProgress.Await -> Unit

                    is ControlledTimerState.InProgress.Running -> {
                        vibrationFromStateProducer.tryVibrate(internalState)
                    }
                }
            }
        }
    }

    private suspend fun onAwaitState() = awaitVibrationJobMutex.withLock {
        val vibrationJob = awaitVibrationJob


        if (vibrationJob != null && vibrationJob.isActive) {
            return@withLock
        }

        awaitVibrationJob = scope.launch(FlipperDispatchers.default) {
            while (isActive) {
                vibratorApi.vibrateOnce(VibrateMode.THUD)
                delay(VibrateMode.THUD.duration)
            }
        }
    }

    override suspend fun onTimerStop() {
        timerStateListenerJob?.cancel()
        vibrationFromStateProducer.clear()
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerStateListener.Factory::class, multibinding = true)
    class Factory(
        val factory: (
            timerApi: TimerApi
        ) -> VibrationTimerListenerImpl
    ) : TimerStateListener.Factory {
        override fun invoke(
            timerApi: TimerApi
        ) = factory(timerApi)
    }
}
