package com.flipperdevices.bsb.appblocker.stats.dao.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.stats.dao.model.DBBlockedAppStat

@Dao
interface AppStatsDao {
    @Insert
    suspend fun insert(appStat: DBBlockedAppStat)

    @Query("SELECT count(*) FROM blocked_stats WHERE app_package == :appPackage AND timestamp > :fromAt")
    suspend fun getLaunchCount(appPackage: String, fromAt: Long): Int

    @Query("DELETE FROM blocked_stats WHERE timestamp < :timestamp")
    suspend fun clearRecordsBefore(timestamp: Long)
}