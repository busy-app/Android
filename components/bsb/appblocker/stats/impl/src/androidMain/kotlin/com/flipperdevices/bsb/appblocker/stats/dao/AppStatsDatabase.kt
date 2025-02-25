package com.flipperdevices.bsb.appblocker.stats.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.appblocker.stats.dao.model.DBBlockedAppStat
import com.flipperdevices.bsb.appblocker.stats.dao.repository.AppStatsDao

@Database(
    entities = [
        DBBlockedAppStat::class
    ],
    autoMigrations = [],
    version = 1,
    exportSchema = true
)
abstract class AppStatsDatabase : RoomDatabase() {
    abstract fun statsDao(): AppStatsDao
}
