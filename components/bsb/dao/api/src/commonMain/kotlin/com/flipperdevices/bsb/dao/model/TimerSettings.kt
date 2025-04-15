package com.flipperdevices.bsb.dao.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class CardSettingsId(val id: Long)

@Serializable
data class CardSettings(
    @SerialName("card_id")
    val id: CardSettingsId,
    @SerialName("title")
    val title: String,
    @SerialName("timer_settings")
    val timerSettings: TimerSettings
) {
    @Serializable
    sealed interface TimerSettings {
        @Serializable
        @SerialName("SIMPLE")
        data class Simple(
            @SerialName("total_time_ms")
            val totalTimeMs: Long
        ) : TimerSettings

        @Serializable
        @SerialName("INTERVAL")
        data class Interval(
            @SerialName("interval_work_ms")
            val workMs: Long,
            @SerialName("interval_rest_ms")
            val restMs: Long,
            @SerialName("interval_cycles_count")
            val cycles: Int,
            @SerialName("is_autostart_enabled")
            val isAutostartEnabled: Boolean
        ) : TimerSettings

        @Serializable
        @SerialName("INFINITE")
        data object Infinite : TimerSettings
    }
}

data class AndroidCardSettings(
    val id: CardSettingsId,
    val sound: SoundSettings
) {
    data class SoundSettings(
        val alertWhenIntervalEnds: Boolean = true
    )
}
