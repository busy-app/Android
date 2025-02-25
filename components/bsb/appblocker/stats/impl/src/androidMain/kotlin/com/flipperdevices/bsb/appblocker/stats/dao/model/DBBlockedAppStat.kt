package com.flipperdevices.bsb.appblocker.stats.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blocked_stats",
    indices = [
        Index(
            value = ["app_package", "timestamp_seconds"],
            unique = true
        )
    ]
)
data class DBBlockedAppStat(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "app_package")
    val appPackage: String,
    @ColumnInfo(name = "timestamp_seconds")
    val timestampSeconds: Long
)
