package com.flipperdevices.bsb.dao.model.cards

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.flipperdevices.bsb.dao.model.converters.AppBlockerStateConverter

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
    @ColumnInfo(name = "apps_blocked_state")
    val isAllAppsBlocked: AppBlockerStateConverter,
)