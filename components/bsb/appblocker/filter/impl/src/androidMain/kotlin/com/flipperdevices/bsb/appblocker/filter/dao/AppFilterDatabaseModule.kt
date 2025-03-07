package com.flipperdevices.bsb.appblocker.filter.dao

import android.content.Context
import androidx.room.Room
import com.flipperdevices.bsb.core.files.Databases
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

private val DATABASE_NAME = Databases.APPBLOCKER_FILTER.filename

@ContributesTo(AppGraph::class)
interface AppFilterDatabaseModule {
    @Provides
    @SingleIn(AppGraph::class)
    fun provideRoom(
        context: Context,
    ): AppFilterDatabase {
        return Room.databaseBuilder(
            context,
            AppFilterDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset("all_selected_categories.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
