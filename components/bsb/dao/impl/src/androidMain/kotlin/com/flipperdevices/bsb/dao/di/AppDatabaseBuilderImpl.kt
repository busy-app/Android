package com.flipperdevices.bsb.dao.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppDatabaseBuilder::class)
class AppDatabaseBuilderImpl(
    private val context: Context
) : AppDatabaseBuilder {
    override fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database.db"
        )
    }
}
