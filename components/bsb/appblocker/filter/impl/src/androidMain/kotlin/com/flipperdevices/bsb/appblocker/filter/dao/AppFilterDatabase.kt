package com.flipperdevices.bsb.appblocker.filter.dao


import androidx.room.Database
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedApp
import com.flipperdevices.bsb.appblocker.filter.dao.repository.AppInformationDAO

@Database(
    entities = [
        DBBlockedApp::class,
    ],
    autoMigrations = [],
    version = 1,
    exportSchema = true
)
abstract class AppFilterDatabase : RoomDatabase() {
    abstract fun keyDao(): AppInformationDAO
}
