package com.flipperdevices.bsb.appblocker.filter.dao


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBAppInformation
import com.flipperdevices.bsb.appblocker.filter.dao.repository.AppInformationDAO
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Database(
    entities = [
        DBAppInformation::class,
    ],
    autoMigrations = [],
    version = 1,
    exportSchema = true
)
abstract class AppFilterDatabase : RoomDatabase() {
    abstract fun keyDao(): AppInformationDAO
}
