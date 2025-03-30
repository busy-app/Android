package com.flipperdevices.bsb.dao.di

import androidx.room.RoomDatabase
import com.flipperdevices.bsb.dao.model.AppDatabase

interface AppDatabaseBuilder {
    fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}
