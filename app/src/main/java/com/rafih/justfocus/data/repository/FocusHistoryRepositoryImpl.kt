package com.rafih.justfocus.data.repository

import android.util.Log
import com.rafih.justfocus.data.local.room.dao.FocusHistoryDao
import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.model.FocusHistoryData
import com.rafih.justfocus.domain.repository.FocusHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.mapKeys

@Singleton
class FocusHistoryRepositoryImpl @Inject constructor(private val dao: FocusHistoryDao): FocusHistoryRepository{

    override suspend fun addFocusHistory(focusHistory: FocusHistory): Long {
        return dao.insertFocusHistory(focusHistory)
    }

    override fun getFocusHistory(): Flow<List<FocusHistoryData>> {
        return dao.fetchFocusHistory().map { data ->
            data.groupBy { it.timeMillsThisDay }.map { data ->

                val tempCal = Calendar.getInstance().apply {
                    timeInMillis = data.key
                }
                val focusDurationTotalMills = data.value.sumOf { it.focusTimeMillsStop - it.focusTimeMillsStart }

                FocusHistoryData(tempCal, focusDurationTotalMills, data.value)
            }
        }
    }

}