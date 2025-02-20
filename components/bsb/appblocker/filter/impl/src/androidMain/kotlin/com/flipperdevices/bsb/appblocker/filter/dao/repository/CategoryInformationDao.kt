package com.flipperdevices.bsb.appblocker.filter.dao.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryInformationDao {
    @Query("SELECT * FROM blocked_category")
    fun getCheckedCategory(): List<DBBlockedCategory>

    @Query("SELECT * FROM blocked_category")
    fun getCheckedCategoryFlow(): Flow<List<DBBlockedCategory>>

    @Query("SELECT * FROM blocked_category WHERE id == :categoryId")
    suspend fun find(categoryId: Int): List<DBBlockedCategory>

    @Insert
    suspend fun insert(category: DBBlockedCategory)

    @Query("DELETE FROM blocked_category")
    suspend fun dropTable()
}
