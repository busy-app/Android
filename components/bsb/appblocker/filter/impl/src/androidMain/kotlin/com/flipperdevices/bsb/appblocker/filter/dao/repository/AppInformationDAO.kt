package com.flipperdevices.bsb.appblocker.filter.dao.repository

import androidx.room.Dao
import androidx.room.Query
import com.flipperdevices.bsb.appblocker.filter.dao.model.DBAppInformation

@Dao
interface AppInformationDAO {
    @Query("SELECT * FROM apps")
    suspend fun getAll(): List<DBAppInformation>
}