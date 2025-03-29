package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CompositeTimerStateListener(
    @Assisted timerApi: TimerApi,
    buildInListeners: Set<TimerStateListener.Factory>
) : TimerStateListener {
    private var listeners = buildInListeners.map {
        it.invoke(timerApi)
    }.toPersistentList()

    fun addListener(listener: TimerStateListener) {
        listeners += listener
    }

    fun removeListener(listener: TimerStateListener) {
        listeners -= listener
    }

    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        listeners.forEach { listener ->
            runCatching {
                listener.onTimerStart(timerSettings)
            }
        }
    }

    override suspend fun onTimerStop() {
        listeners.forEach { listener ->
            runCatching {
                listener.onTimerStop()
            }
        }
    }
}
