package com.flipperdevices.bsb.dao.model.blocked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.flipperdevices.bsb.dao.model.cards.DBCardEntity

@Entity(
    tableName = "blocked_category",
    primaryKeys = ["category_id", "card_id"],
    foreignKeys = [
        ForeignKey(
            entity = DBCardEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("card_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DBBlockedCategory(
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "card_id")
    val cardId: Long
)
