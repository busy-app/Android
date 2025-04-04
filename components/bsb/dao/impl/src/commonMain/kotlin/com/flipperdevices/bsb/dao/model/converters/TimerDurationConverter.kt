package com.flipperdevices.bsb.dao.model.converters

import androidx.room.TypeConverter
import com.flipperdevices.bsb.dao.model.TimerDuration
import kotlin.time.Duration.Companion.milliseconds

class TimerDurationConverter {
    @TypeConverter
    fun fromLong(value: Long?): TimerDuration? {
        return when (value) {
            -1L -> TimerDuration.Infinite
            null -> null
            else -> TimerDuration.Finite(value.milliseconds)
        }
    }

    @TypeConverter
    fun toLong(value: TimerDuration?): Long? {
        return when (value) {
            is TimerDuration.Finite -> value.instance.inWholeMilliseconds
            TimerDuration.Infinite -> -1
            null -> null
        }
    }
}
