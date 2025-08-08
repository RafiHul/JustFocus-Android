package com.rafih.justfocus.domain.usecase.stopwatch

import android.icu.util.Calendar
import android.util.Log
import com.rafih.justfocus.data.repository.FocusHistoryRepositoryImpl
import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.FocusModeSessionInfo
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartStopwatchUseCase @Inject constructor(
    private val stopwatchRepository: StopwatchRepositoryImpl,
    private val focusHistoryRepositoryImpl: FocusHistoryRepositoryImpl
){
    suspend operator fun invoke(activity: String) {
        val cacheFocusInfo = focusHistoryRepositoryImpl.cacheFocusModeSessionInfo

        if (cacheFocusInfo == null){ //ini cuma bisa sekali pas pertama kali menyalakan focus mode
            Log.d("cek null", "nullll")
            focusHistoryRepositoryImpl.setCacheFocusMode(FocusModeSessionInfo(
                startMills = Calendar.getInstance().timeInMillis,
                activity = activity
            ))
        }

        stopwatchRepository.bindService()
        stopwatchRepository.serviceConnected.first { it }
        stopwatchRepository.startStopwatch()
    }
}