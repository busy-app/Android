package com.flipperdevices.bsb.dao.model

import com.flipperdevices.bsb.dao.serialization.TimerDurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@JvmInline
@Serializable
value class TimerSettingsId(val id: Long)

@Serializable
data class TimerSettings(
    @SerialName("id")
    val id: TimerSettingsId,
    @SerialName("total_time")
    @Serializable(TimerDurationSerializer::class)
    val totalTime: TimerDuration = TimerDuration.Finite(90.minutes),
    @SerialName("intervals_settings")
    val intervalsSettings: IntervalsSettings = IntervalsSettings(),
    @SerialName("sound_settings")
    val soundSettings: SoundSettings = SoundSettings(),
    @SerialName("name")
    val name: String = "BUSY",
) {
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

    @Serializable
    data class SoundSettings(
        @SerialName("alert_when_interval_ends")
        val alertWhenIntervalEnds: Boolean = true
    )
}
