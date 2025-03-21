package com.flipperdevices.bsb.dao.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File

@ContributesTo(AppGraph::class)
interface AppDatabaseBuilderModule {
    @Provides
    fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "busyapp_database.db")
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}
