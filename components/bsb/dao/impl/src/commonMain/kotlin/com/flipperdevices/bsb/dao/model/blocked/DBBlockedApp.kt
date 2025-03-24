package com.flipperdevices.bsb.dao.model.blocked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.flipperdevices.bsb.dao.model.cards.DBCardEntity

@Entity(
    tableName = "blocked_apps",
    primaryKeys = ["app_package", "card_id"],
    foreignKeys = [
        ForeignKey(
            entity = DBCardEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("card_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("card_id")
    ]
)
data class DBBlockedApp(
    @ColumnInfo(name = "app_package")
    val appPackage: String,
    @ColumnInfo(name = "card_id")
    val cardId: Long
)
