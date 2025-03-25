package com.flipperdevices.bsb.dao.di

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info

class FirstMigrationCallback : RoomDatabase.Callback(), LogTagProvider {
    override val TAG = "FirstMigrationCallback"

    private var dao: AppDatabase? = null

    fun setDAO(dao: AppDatabase) {
        this.dao = dao
    }

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        info { "#onCreate database with $dao" }
    }
}