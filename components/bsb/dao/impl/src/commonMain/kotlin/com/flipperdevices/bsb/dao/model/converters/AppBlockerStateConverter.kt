package com.flipperdevices.bsb.dao.model.converters

import androidx.room.TypeConverter
import com.flipperdevices.bsb.dao.model.cards.AppBlockerState

class AppBlockerStateConverter {
    @TypeConverter
    fun fromInt(value: Int?): AppBlockerState? {
        return value?.let { enumValues<AppBlockerState>().getOrNull(it) }
    }

    @TypeConverter
    fun toInt(value: AppBlockerState?): Int? {
        return value?.ordinal
    }
}