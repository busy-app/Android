package com.flipperdevices.bsb.dao.model.cards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DBCardRepository {
    @Query("SELECT * FROM cards")
    fun getTimerSettingsFlow(): Flow<List<DBCardEntity>>

    @Query("SELECT * FROM cards_platform_settings WHERE card_id == :cardId")
    fun getPlatformSpecificSetting(cardId: Long): Flow<DBCardPlatformSpecificSettings>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: DBCardEntity)
}