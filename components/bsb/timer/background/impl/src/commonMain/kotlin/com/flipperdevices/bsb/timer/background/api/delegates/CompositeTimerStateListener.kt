package com.flipperdevices.bsb.timer.background.api.delegates

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import me.tatarka.inject.annotations.Inject

@Inject
class CompositeTimerStateListener(
    buildInListeners: Set<TimerStateListener>
) : TimerStateListener {
    private var listeners = buildInListeners.toPersistentList()

    fun addListener(listener: TimerStateListener) {
        listeners += listener
    }

    fun removeListener(listener: TimerStateListener) {
        listeners -= listener
    }

    override fun onTimerStart(timerSettings: TimerSettings) {
        listeners.forEach { listener ->
            runCatching {
                listener.onTimerStart(timerSettings)
            }
        }
    }

    override fun onTimerStop() {
        listeners.forEach { listener ->
            runCatching {
                listener.onTimerStop()
            }
        }
    }
}
