package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerTimestamp {
    val lastSync: Instant

    @Serializable
    data class Pending(override val lastSync: Instant = Instant.DISTANT_PAST) : TimerTimestamp

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

    val runningOrNull: TimerTimestamp.Running?
        get() = this as? Running
}
