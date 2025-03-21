package com.flipperdevices.bsb.dao.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flipperdevices.bsb.dao.model.blocked.DBBlockedApp
import com.flipperdevices.bsb.dao.model.blocked.DBBlockedCategory
import com.flipperdevices.bsb.dao.model.cards.DBCardEntity
import com.flipperdevices.bsb.dao.model.converters.DurationConverter
import com.flipperdevices.bsb.dao.model.stats.DBBlockedAppStat


@Database(
    entities = [
        DBCardEntity::class,
        DBBlockedCategory::class,
        DBBlockedApp::class,
        DBBlockedAppStat::class
    ],
    autoMigrations = [],
    version = 1,
    exportSchema = true
)
@TypeConverters(DurationConverter::class)
abstract class AppDatabase : RoomDatabase() {
}