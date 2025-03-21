package com.flipperdevices.bsb.dao.api.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.serialization.Serializable

@Serializable
data class TimerSettings(
    val id: Long = 0,
    val totalTime: Duration = 90.minutes,
    val intervalsSettings: IntervalsSettings = IntervalsSettings(),
    val soundSettings: SoundSettings = SoundSettings(),
    val name: String = "BUSY"
) {
    @Serializable
    data class IntervalsSettings(
        val work: Duration = 25.minutes,
        val rest: Duration = 5.minutes,
        val longRest: Duration = 15.minutes,
        val autoStartWork: Boolean = true,
        val autoStartRest: Boolean = true,
        val isEnabled: Boolean = false
    )

    @Serializable
    data class SoundSettings(
        val alertWhenIntervalEnds: Boolean = true
    )
}
