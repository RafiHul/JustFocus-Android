package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.data.model.BlockedShort

@Dao
interface BlockedShortDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBatchBlockedShort(blockedAppList: List<BlockedShort>)

    @Query("DELETE FROM blocked_short")
    suspend fun deleteAllBlockedShort()

    @Query("SELECT * FROM blocked_short")
    suspend fun getBlockedShort(): List<BlockedApp>
}