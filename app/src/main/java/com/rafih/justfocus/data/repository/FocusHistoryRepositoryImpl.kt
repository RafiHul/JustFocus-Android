package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.room.dao.FocusHistoryDao
import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.repository.FocusHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusHistoryRepositoryImpl @Inject constructor(private val dao: FocusHistoryDao): FocusHistoryRepository{

    override suspend fun addFocusHistory(focusHistory: FocusHistory): Long {
        return dao.insertFocusHistory(focusHistory)
    }

    override fun getFocusHistory(): Flow<Map<Long, List<FocusHistory>>> {
        return dao.fetchFocusHistory().map { data ->
            data.groupBy { it.timeMillsThisDay }
        }
    }

}