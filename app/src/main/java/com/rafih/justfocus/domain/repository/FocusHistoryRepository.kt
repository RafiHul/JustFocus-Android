package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.FocusHistory
import kotlinx.coroutines.flow.Flow

interface FocusHistoryRepository {
    suspend fun addFocusHistory(focusHistory: FocusHistory): Long
    fun getFocusHistory(): Flow<Map<Long, List<FocusHistory>>>
}