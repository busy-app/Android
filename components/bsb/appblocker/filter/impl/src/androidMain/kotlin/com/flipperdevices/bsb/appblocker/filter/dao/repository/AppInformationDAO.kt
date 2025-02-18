package com.flipperdevices.bsb.appblocker.filter.dao.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedApp

@Dao
interface AppInformationDAO {
    @Query("SELECT * FROM blocked_apps")
    fun getCheckedApps(): List<DBBlockedApp>

    @Insert
    suspend fun insert(appInformation: DBBlockedApp)

    @Query("DELETE FROM blocked_apps")
    suspend fun dropTable()
}