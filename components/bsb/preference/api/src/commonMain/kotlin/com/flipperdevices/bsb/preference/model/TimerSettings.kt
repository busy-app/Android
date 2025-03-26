package com.flipperdevices.bsb.preference.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Serializable
data class TimerSettings(
    @SerialName("total_time")
    val totalTime: Duration = 90.minutes,
    @SerialName("intervals_settings")
    val intervalsSettings: IntervalsSettings = IntervalsSettings(),
    @SerialName("sound_settings")
    val soundSettings: SoundSettings = SoundSettings()
) {
    // todo no card name set yet
    @SerialName("name")
    val name: String = "BUSY"

    @Serializable
    data class IntervalsSettings(
        @SerialName("work")
        val work: Duration = 25.minutes,
        @SerialName("rest")
        val rest: Duration = 5.minutes,
        @SerialName("long_rest")
        val longRest: Duration = 15.minutes,
        @SerialName("auto_start_work")
        val autoStartWork: Boolean = true,
        @SerialName("auto_start_rest")
        val autoStartRest: Boolean = true,
        @SerialName("is_enabled")
        val isEnabled: Boolean = false
    )

    @Transient
    val totalIterations: Int
        get() = when {
            intervalsSettings.isEnabled -> {
                totalTime
                    .inWholeSeconds
                    .div(
                        intervalsSettings
                            .work
                            .plus(intervalsSettings.rest)
                            .inWholeSeconds
                    ).toInt()
            }

            else -> 0
        }

    @Serializable
    data class SoundSettings(
        @SerialName("alert_when_interval_ends")
        val alertWhenIntervalEnds: Boolean = true
    )
}

