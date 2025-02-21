package com.flipperdevices.bsb.appblocker.filter.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedApp
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedCategory
import com.flipperdevices.bsb.appblocker.filter.dao.repository.AppInformationDao
import com.flipperdevices.bsb.appblocker.filter.dao.repository.CategoryInformationDao

@Database(
    entities = [
        DBBlockedApp::class,
        DBBlockedCategory::class
    ],
    autoMigrations = [],
    version = 1,
    exportSchema = true
)
abstract class AppFilterDatabase : RoomDatabase() {
    abstract fun appDao(): AppInformationDao
    abstract fun categoryDao(): CategoryInformationDao
}
