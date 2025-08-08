package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.model.FocusHistoryData
import com.rafih.justfocus.domain.model.FocusModeSessionInfo
import kotlinx.coroutines.flow.Flow

interface FocusHistoryRepository {
    var cacheFocusModeSessionInfo: FocusModeSessionInfo?
    suspend fun addFocusHistory(focusHistory: FocusHistory): Long
    fun getFocusHistory(): Flow<List<FocusHistoryData>>
    fun setCacheFocusMode(focusModeSessionInfo: FocusModeSessionInfo?)
}