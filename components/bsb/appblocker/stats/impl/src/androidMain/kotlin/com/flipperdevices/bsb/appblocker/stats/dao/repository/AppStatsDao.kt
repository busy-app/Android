package com.flipperdevices.bsb.appblocker.stats.dao.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.flipperdevices.bsb.appblocker.stats.dao.model.DBBlockedAppStat

@Dao
interface AppStatsDao {
    @Upsert
    suspend fun insert(appStat: DBBlockedAppStat)

    @Query("SELECT count(*) FROM blocked_stats WHERE app_package == :appPackage AND timestamp_seconds > :fromAtSeconds")
    suspend fun getLaunchCount(appPackage: String, fromAtSeconds: Long): Int

    @Query("DELETE FROM blocked_stats WHERE timestamp_seconds < :timestampSeconds")
    suspend fun clearRecordsBefore(timestampSeconds: Long)
}
