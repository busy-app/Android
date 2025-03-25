package com.flipperdevices.bsb.dao.di

import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppGraph::class)
interface AppDatabaseModule {
    @Provides
    @SingleIn(AppGraph::class)
    fun provideAppDatabase(
        builder: AppDatabaseBuilder,
        preferenceApi: PreferenceApi,
        scope: CoroutineScope
    ): AppDatabase {
        val callback = FirstMigrationCallback(preferenceApi, scope)

        val database = builder.provideAppDatabaseBuilder()
            .addCallback(callback)
            .build()

        callback.setDAO(database)

        return database
    }
}