package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rafih.justfocus.data.model.AppUsageLimit

@Dao
interface AppUsageLimitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(appUsageLimit: AppUsageLimit)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBatch(appUsageLimitList: List<AppUsageLimit>)
    @Update
    suspend fun update(appUsageLimit: AppUsageLimit)
    @Delete
    suspend fun delete(appUsageLimit: AppUsageLimit)
    @Query("DELETE FROM app_usage_limit WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String)
    @Query("SELECT * FROM app_usage_limit")
    suspend fun get(): List<AppUsageLimit>
}