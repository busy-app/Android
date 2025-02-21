package com.flipperdevices.bsb.appblocker.filter.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "blocked_category",
    indices = []
)
data class DBBlockedCategory(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val categoryId: Int,
)
