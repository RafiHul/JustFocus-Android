package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.model.FocusHistoryData
import kotlinx.coroutines.flow.Flow

interface FocusHistoryRepository {
    suspend fun addFocusHistory(focusHistory: FocusHistory): Long
    fun getFocusHistory(): Flow<List<FocusHistoryData>>
}