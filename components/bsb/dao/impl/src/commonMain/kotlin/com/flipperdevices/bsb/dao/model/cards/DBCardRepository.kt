package com.flipperdevices.bsb.dao.model.cards

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DBCardRepository {
    @Query("SELECT * FROM cards")
    fun getTimerSettingsListFlow(): Flow<List<DBCardEntity>>

    @Query("SELECT * FROM cards WHERE id == :cardId")
    fun getTimerSettingsFlow(cardId: Long): Flow<DBCardEntity?>

    @Query("SELECT * FROM cards_platform_settings WHERE card_id == :cardId")
    fun getPlatformSpecificSettingFlow(cardId: Long): Flow<DBCardPlatformSpecificSettings?>

    @Query("SELECT * FROM cards_platform_settings WHERE card_id == :cardId")
    fun getPlatformSpecificSetting(cardId: Long): DBCardPlatformSpecificSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(platformSpecificSettings: DBCardPlatformSpecificSettings)

    @Transaction
    suspend fun updateAppBlockingState(
        cardId: Long,
        appBlockerState: AppBlockerState
    ) {
        val settings = getPlatformSpecificSetting(cardId)
        val newSettings = settings?.copy(appBlockerState = appBlockerState)
            ?: DBCardPlatformSpecificSettings(
                cardId = cardId,
                appBlockerState = appBlockerState
            )
        insert(newSettings)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: DBCardEntity): Long
}