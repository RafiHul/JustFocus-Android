package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafih.justfocus.data.model.AppTimer

@Dao
interface AppTimerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBatchAppTimer(appTimerList: List<AppTimer>)

    @Delete
    suspend fun deleteAppTimer(appTimer: AppTimer)

    @Query("SELECT * FROM app_timer")
    suspend fun getAppTimer(): List<AppTimer>
}