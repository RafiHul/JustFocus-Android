package com.rafih.justfocus.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rafih.justfocus.data.model.FocusHistory

@Dao
interface FocusHistoryDao {
    @Insert
    suspend fun insertFocusHistory(focusHistory: FocusHistory): Long
}