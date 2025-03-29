package com.flipperdevices.bsb.dao.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import java.io.File

@Inject
@ContributesBinding(AppGraph::class, AppDatabaseBuilder::class)
class AppDatabaseBuilderImpl : AppDatabaseBuilder {
    override fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "busyapp_database.db")
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}
