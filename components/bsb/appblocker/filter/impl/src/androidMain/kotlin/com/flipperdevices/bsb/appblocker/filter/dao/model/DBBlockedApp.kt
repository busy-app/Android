package com.flipperdevices.bsb.appblocker.filter.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "blocked_apps",
    indices = []
)
data class DBBlockedApp(
    @ColumnInfo(name = "app_package")
    @PrimaryKey
    val appPackage: String,
)
