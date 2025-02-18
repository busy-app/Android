package com.flipperdevices.bsb.appblocker.filter.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps",
    indices = [
        Index(
            value = ["app_package"],
            unique = true
        ),
    ]
)
data class DBAppInformation(
    @ColumnInfo(name = "uid")
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "app_package")
    val appPackage: String,
)