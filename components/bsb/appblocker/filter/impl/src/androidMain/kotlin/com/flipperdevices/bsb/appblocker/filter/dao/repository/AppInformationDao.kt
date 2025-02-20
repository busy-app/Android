package com.flipperdevices.bsb.appblocker.filter.dao.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedApp
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInformationDao {
    @Query("SELECT * FROM blocked_apps")
    fun getCheckedApps(): List<DBBlockedApp>

    @Query("SELECT * FROM blocked_apps")
    fun getCheckedAppsFlow(): Flow<List<DBBlockedApp>>

    @Query("SELECT count(*) FROM blocked_apps")
    fun getCheckedAppsCountFlow(): Flow<Int>

    @Query("SELECT * FROM blocked_apps WHERE app_package == :packageName")
    suspend fun find(packageName: String): List<DBBlockedApp>

    @Insert
    suspend fun insert(appInformation: DBBlockedApp)

    @Query("DELETE FROM blocked_apps")
    suspend fun dropTable()
}
