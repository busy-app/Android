package com.flipperdevices.bsb.dao.model.cards

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards_platform_settings",
    foreignKeys = [
        ForeignKey(
            entity = DBCardEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("card_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DBCardPlatformSpecificSettings(
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val cardId: Long,
    @ColumnInfo(name = "is_blocked_enabled")
    val isBlockedEnabled: Boolean = true,
    @ColumnInfo(name = "is_block_all")
    val isBlockedAll: Boolean = true
)