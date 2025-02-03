package com.flipperdevices.bsb.preference.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class TimerSettings(
    val timer: Duration = 25.minutes,
    val intervalsSettings: IntervalsSettings = IntervalsSettings()
) {
    @Serializable
    data class IntervalsSettings(
        val rest: Duration = 5.minutes,
        val longRest: Duration = 5.minutes,
        val cycles: Int = 2,
        val isEnabled: Boolean = false
    )
}
