package com.flipperdevices.bsb.preference.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Serializable
data class TimerSettings(
    val totalTime: Duration = 90.minutes,
    val intervalsSettings: IntervalsSettings = IntervalsSettings(),
    val soundSettings: SoundSettings = SoundSettings()
) {
    // todo no card name set yet
    val name: String = "BUSY"

    @Serializable
    data class IntervalsSettings(
        val work: Duration = 10.seconds,
        val rest: Duration = 10.seconds,
        val longRest: Duration = 10.seconds,
        val autoStartWork: Boolean = true,
        val autoStartRest: Boolean = true,
        val isEnabled: Boolean = true
    )

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
        val alertWhenIntervalEnds: Boolean = true
    )
}
