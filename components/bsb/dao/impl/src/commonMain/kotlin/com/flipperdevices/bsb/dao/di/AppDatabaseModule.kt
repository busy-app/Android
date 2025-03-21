package com.flipperdevices.bsb.dao.di

import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppGraph::class)
interface AppDatabaseModule {
    @Provides
    @SingleIn(AppGraph::class)
    fun provideAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
        return builder.build()
    }
}