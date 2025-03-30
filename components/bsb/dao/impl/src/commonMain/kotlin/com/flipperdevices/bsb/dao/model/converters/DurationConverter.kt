package com.flipperdevices.bsb.dao.model.converters

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationConverter {
    @TypeConverter
    fun fromLong(value: Long?): Duration? {
        if (value == null) {
            return null
        }
        return value.milliseconds
    }

    @TypeConverter
    fun toLong(value: Duration?): Long? {
        return value?.inWholeMilliseconds
    }
}
