package com.flipperdevices.bsb.timer.background.model

import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TimerTimestamp(
    val settings: TimerSettings,
    val start: Instant = Clock.System.now(),
    val pause: Instant? = null,
    val confirmNextStepClick: Instant = Instant.DISTANT_PAST
)
