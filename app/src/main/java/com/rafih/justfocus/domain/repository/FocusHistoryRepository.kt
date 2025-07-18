package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.FocusHistory

interface FocusHistoryRepository {
    suspend fun addFocusHistory(focusHistory: FocusHistory): Long
}