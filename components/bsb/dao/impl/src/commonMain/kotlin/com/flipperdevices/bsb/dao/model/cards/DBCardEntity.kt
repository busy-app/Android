package com.flipperdevices.bsb.dao.model.cards

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(
    tableName = "cards",
    indices = []
)
data class DBCardEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("time_total")
    val totalTime: Duration,
    @ColumnInfo("time_work")
    val work: Duration,
    @ColumnInfo("time_rest")
    val rest: Duration,
    @ColumnInfo("time_long_rest")
    val longRest: Duration,
    @ColumnInfo("is_auto_start_work")
    val autoStartWork: Boolean,
    @ColumnInfo("is_auto_start_rest")
    val autoStartRest: Boolean,
    @ColumnInfo("is_interval_enabled")
    val isIntervalEnabled: Boolean,
    @ColumnInfo("is_alert_when_interval_ends")
    val alertWhenIntervalEnds: Boolean,
)