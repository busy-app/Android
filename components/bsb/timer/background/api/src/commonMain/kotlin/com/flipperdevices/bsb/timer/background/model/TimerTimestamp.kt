package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerTimestamp {
    val lastSync: Instant

    @Serializable
    class Pending private constructor(
        override val lastSync: Instant
    ) : TimerTimestamp {
        companion object {
            val NotStarted: Pending
                get() = Pending(Instant.DISTANT_PAST)
            val Finished: Pending
                get() = Pending(Clock.System.now())
        }
    }

    /**
     * [TimerTimestamp] shared synchronization model for timer
     * @param settings timer settings to determine new state
     * @param start time when timer was started
     * @param pause time when pause was clicked
     * @param confirmNextStepClick time when next step was clicked after autopause
     * @param lastSync time when sync of this item was received on device
     */
    @Serializable
    data class Running(
        val settings: TimerSettings,
        val start: Instant = Clock.System.now(),
        val pause: Instant? = null,
        val confirmNextStepClick: Instant = Instant.DISTANT_PAST,
        override val lastSync: Instant
    ) : TimerTimestamp

    val runningOrNull: Running?
        get() = this as? Running
}
