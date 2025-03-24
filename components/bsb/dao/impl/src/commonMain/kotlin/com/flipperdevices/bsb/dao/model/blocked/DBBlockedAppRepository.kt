package com.flipperdevices.bsb.dao.model.blocked

import androidx.room.Dao
import androidx.room.Query
import com.flipperdevices.bsb.dao.model.cards.DBCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DBBlockedAppRepository {
    @Query("SELECT * FROM blocked_apps WHERE card_id == :cardId ")
    fun getBlockedApp(cardId: Long): Flow<List<DBBlockedApp>>

    @Query("SELECT * FROM blocked_category WHERE card_id == :cardId ")
    fun getBlockedCategories(cardId: Long): Flow<List<DBBlockedCategory>>

}