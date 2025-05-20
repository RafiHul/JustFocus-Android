package com.rafih.justfocus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rafih.justfocus.data.model.BlockedApp

@Dao
interface BlockedAppDao {
    @Insert
    suspend fun addBlockedApp(blockedApp: BlockedApp)

    @Delete
    suspend fun deleteBlockedApp(blockedApp: BlockedApp)

    @Update
    suspend fun updateBlockedApp(blockedApp: BlockedApp)

    @Query("SELECT * FROM blocked_app")
    suspend fun getBlockedApp(): List<BlockedApp>
}
