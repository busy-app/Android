package com.flipperdevices.bsb.preference.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class TimerSettings(
    val totalTime: Duration = 90.minutes,
    val intervalsSettings: IntervalsSettings = IntervalsSettings(),
    val soundSettings: SoundSettings = SoundSettings()
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
        val alertBeforeWorkStarts: Boolean = true,
        val alertBeforeWorkEnds: Boolean = true
    )
}
