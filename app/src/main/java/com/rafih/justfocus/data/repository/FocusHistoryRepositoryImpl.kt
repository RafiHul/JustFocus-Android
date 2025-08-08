package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.room.dao.FocusHistoryDao
import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.model.FocusHistoryData
import com.rafih.justfocus.domain.model.FocusModeSessionInfo
import com.rafih.justfocus.domain.repository.FocusHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusHistoryRepositoryImpl @Inject constructor(private val dao: FocusHistoryDao): FocusHistoryRepository{
    override var cacheFocusModeSessionInfo: FocusModeSessionInfo? = null

    override suspend fun addFocusHistory(focusHistory: FocusHistory): Long {
        return dao.insertFocusHistory(focusHistory)
    }

    override fun getFocusHistory(): Flow<List<FocusHistoryData>> {
        return dao.fetchFocusHistory().map { data ->
            data.groupBy { it.timeMillsThisDay }.map { data1 ->

                val tempCal = Calendar.getInstance().apply {
                    timeInMillis = data1.key
                }
                val focusDurationTotalMills = data1.value.sumOf { it.focusTimeMillsStop - it.focusTimeMillsStart }

                FocusHistoryData(tempCal, focusDurationTotalMills, data1.value)
            }
        }
    }

    override fun setCacheFocusMode(focusModeSessionInfo: FocusModeSessionInfo?) {
        cacheFocusModeSessionInfo = focusModeSessionInfo
    }

}