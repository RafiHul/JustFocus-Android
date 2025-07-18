package com.rafih.justfocus.domain.usecase.focushistory

import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.data.repository.FocusHistoryRepositoryImpl
import com.rafih.justfocus.domain.model.handle.RoomResult
import com.rafih.justfocus.domain.changeTimeToMidNight
import com.rafih.justfocus.domain.getYearMonthDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddFocusHistoryUseCase @Inject constructor(
    private val focusHistoryRepository: FocusHistoryRepositoryImpl
) {

    fun execute(focusTimeMillsStart: Long, focusTimeMillsStop: Long): Flow<RoomResult> = flow<RoomResult> {
        val thisDayMidNight = Calendar.getInstance().apply {
            changeTimeToMidNight()
        }
        val thisDayMills = thisDayMidNight.timeInMillis
        val (year, month, day) = thisDayMidNight.getYearMonthDay()

        val focusHistory = FocusHistory(
            0,
            thisDayMills,
            focusTimeMillsStart,
            focusTimeMillsStop,
            day, month, year
        )

        focusHistoryRepository.addFocusHistory(focusHistory)
        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }

}