package com.flipperdevices.bsb.dao.model.blocked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DBBlockedAppRepository {
    @Query("SELECT * FROM blocked_apps WHERE card_id == :cardId ")
    fun getBlockedApps(cardId: Long): Flow<List<DBBlockedApp>>

    @Query("SELECT * FROM blocked_apps WHERE card_id == :cardId AND app_package == :appPackage")
    suspend fun getBlockedApp(cardId: Long, appPackage: String): DBBlockedApp?

    @Query("SELECT * FROM blocked_category WHERE card_id == :cardId ")
    fun getBlockedCategories(cardId: Long): Flow<List<DBBlockedCategory>>

    @Transaction
    suspend fun replace(
        cardId: Long,
        entities: List<BlockedAppEntity>
    ) {
        dropApp(cardId)
        dropCategory(cardId)

        entities.forEach {
            when (it) {
                is BlockedAppEntity.App -> insert(
                    DBBlockedApp(
                        appPackage = it.packageId,
                        cardId = cardId
                    )
                )

                is BlockedAppEntity.Category -> insert(
                    DBBlockedCategory(
                        categoryId = it.categoryId,
                        cardId = cardId
                    )
                )
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: DBBlockedCategory)

    @Query("DELETE FROM blocked_category WHERE card_id == :cardId")
    suspend fun dropCategory(cardId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: DBBlockedApp)

    @Query("DELETE FROM blocked_apps WHERE card_id == :cardId")
    suspend fun dropApp(cardId: Long)
}
