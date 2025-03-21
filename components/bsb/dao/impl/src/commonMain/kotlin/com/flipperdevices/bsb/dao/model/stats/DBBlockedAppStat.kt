package com.flipperdevices.bsb.dao.model.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.flipperdevices.bsb.dao.model.cards.DBCardEntity

@Entity(
    tableName = "blocked_stats",
    indices = [
        Index(
            value = ["timestamp_seconds", "app_package"],
            unique = true
        ),
        Index(
            value = ["card_id", "timestamp_seconds", "app_package"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = DBCardEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("card_id"),
            onDelete = ForeignKey.CASCADE
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
    val timestampSeconds: Long,
    @ColumnInfo(name = "card_id")
    val cardId: Long
)
