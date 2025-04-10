package com.flipperdevices.bsb.dao.model.cards

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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
    suspend fun getPlatformSpecificSetting(cardId: Long): DBCardPlatformSpecificSettings?

    @Upsert
    suspend fun insert(platformSpecificSettings: DBCardPlatformSpecificSettings)

    @Transaction
    suspend fun updateBlockedEnabled(
        cardId: Long,
        isBlockedEnabled: Boolean
    ) {
        val settings = getPlatformSpecificSetting(cardId)
        val newSettings = settings?.copy(isBlockedEnabled = isBlockedEnabled)
            ?: DBCardPlatformSpecificSettings(
                cardId = cardId,
                isBlockedEnabled = isBlockedEnabled
            )
        insert(newSettings)
    }

    @Transaction
    suspend fun updateBlockedAll(
        cardId: Long,
        isBlockedAll: Boolean
    ) {
        val settings = getPlatformSpecificSetting(cardId)
        val newSettings = settings?.copy(isBlockedAll = isBlockedAll)
            ?: DBCardPlatformSpecificSettings(
                cardId = cardId,
                isBlockedAll = isBlockedAll
            )
        insert(newSettings)
    }

    @Upsert
    suspend fun insertOrUpdate(settings: DBCardEntity): Long
}
