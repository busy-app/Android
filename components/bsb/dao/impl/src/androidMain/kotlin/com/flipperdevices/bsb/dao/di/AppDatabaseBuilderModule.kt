package com.flipperdevices.bsb.dao.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppGraph::class)
interface AppDatabaseBuilderModule {
    @Provides
    fun provideAppDatabaseBuilder(
        context: Context,
    ): RoomDatabase.Builder<AppDatabase> {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database.db"
        )
    }
}