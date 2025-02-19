package com.flipperdevices.bsb.appblocker.filter.dao.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBBlockedCategory

@Dao
interface CategoryInformationDao {
    @Query("SELECT * FROM blocked_category")
    fun getCheckedCategory(): List<DBBlockedCategory>

    @Insert
    suspend fun insert(category: DBBlockedCategory)

    @Query("DELETE FROM blocked_category")
    suspend fun dropTable()
}