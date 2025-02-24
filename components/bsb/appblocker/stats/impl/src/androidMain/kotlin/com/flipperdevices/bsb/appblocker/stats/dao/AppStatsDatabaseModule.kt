package com.flipperdevices.bsb.appblocker.stats.dao

import android.content.Context
import androidx.room.Room
import com.flipperdevices.bsb.core.files.Databases
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppGraph::class)
interface AppStatsDatabaseModule {
    @Provides
    @SingleIn(AppGraph::class)
    fun provideAppBlockerStatsRoom(
        context: Context,
    ): AppStatsDatabase {
        return Room.databaseBuilder(
            context,
            AppStatsDatabase::class.java,
            Databases.APPBLOCKER_STATS.filename
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
