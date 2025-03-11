package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.info
import kotlin.math.log
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * [TimerTimestamp] shared synchronization model for timer
 * @param settings timer settings to determine new state
 * @param start time when timer was started
 * @param pause time when pause was clicked
 * @param confirmNextStepClick time when next step was clicked after autopause
 * @param lastSync time when sync of this item was received on device
 */
@Serializable
data class TimerTimestamp(
    val settings: TimerSettings,
    val start: Instant = Clock.System.now(),
    val pause: Instant? = null,
    val confirmNextStepClick: Instant = Instant.DISTANT_PAST,
    val lastSync: Instant = Instant.DISTANT_PAST
)

private val logger = TaggedLogger("TimerTimestampKt")

fun TimerTimestamp?.compareAndGetState(other: TimerTimestamp?): TimerTimestamp? {
    val oldState = this
    val state = other
    logger.info { "#compareAndGetState old: $oldState; new: $state" }
    return when {
        state == null -> oldState
        oldState == null -> state
        state.lastSync > oldState.lastSync -> state
        else -> oldState
    }
}
