package com.flipperdevices.bsb.dao.model.cards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface DBCardRepository {
    @Query("SELECT * FROM cards")
    fun getTimerSettingsListFlow(): Flow<List<DBCardEntity>>

    @Query("SELECT * FROM cards WHERE id == :cardId")
    fun getTimerSettingsFlow(cardId: Long): Flow<DBCardEntity?>

    @Query("SELECT * FROM cards_platform_settings WHERE card_id == :cardId")
    fun getPlatformSpecificSetting(cardId: Long): Flow<DBCardPlatformSpecificSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: DBCardEntity)
}