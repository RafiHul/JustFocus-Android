package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafih.justfocus.data.model.FocusHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusHistoryDao {
    @Insert
    suspend fun insertFocusHistory(focusHistory: FocusHistory): Long

    @Query("SELECT * FROM focus_history")
    fun fetchFocusHistory(): Flow<List<FocusHistory>>
}