package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafih.justfocus.data.model.BlockedApp

@Dao
interface BlockedAppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBlockedApp(blockedApp: BlockedApp)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBatchBlockedApp(blockedAppList: List<BlockedApp>)

    @Query("DELETE FROM blocked_app")
    suspend fun deleteAllBlockedApp()

    @Query("SELECT * FROM blocked_app")
    suspend fun getBlockedApp(): List<BlockedApp>
}
