package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("TIMER_TIMESTAMP")
sealed interface TimerTimestamp {
    @SerialName("last_sync")
    val lastSync: Instant

    @Serializable
    @SerialName("PENDING")
    sealed interface Pending : TimerTimestamp {
        @Serializable
        @SerialName("NotStarted")
        data object NotStarted : Pending {
            override val lastSync = Instant.DISTANT_PAST
        }

        @Serializable
        @SerialName("Finished")
        data class Finished(
            @SerialName("last_sync")
            override val lastSync: Instant
        ) : Pending
    }

    /**
     * [TimerTimestamp] shared synchronization model for timer
     * @param settings timer settings to determine new state
     * @param start time when timer was started
     * @param noOffsetStart is real time of when timer started. Shouldn't be changed after timer start
     * @param pause time when pause was clicked
     * @param confirmNextStepClick time when next step was clicked after autopause
     * @param lastSync time when sync of this item was received on device
     */
    @Serializable
    @SerialName("RUNNING")
    data class Running(
        @SerialName("settings")
        val settings: TimerSettings,
        @SerialName("start")
        val start: Instant,
        @SerialName("no_offset_start")
        val noOffsetStart: Instant,
        @SerialName("pause")
        val pause: Instant? = null,
        @SerialName("confirm_next_step_click")
        val confirmNextStepClick: Instant = Instant.DISTANT_PAST,
        @SerialName("last_sync")
        override val lastSync: Instant
    ) : TimerTimestamp

    @Transient
    val runningOrNull: Running?
        get() = this as? Running
}
