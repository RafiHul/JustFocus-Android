package com.rafih.justfocus.domain.usecase.stopwatch

import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.FocusModeSessionDuration
import com.rafih.justfocus.domain.model.handle.RoomResult
import com.rafih.justfocus.domain.usecase.blockedapp.DeleteBlockedAppUseCase
import com.rafih.justfocus.domain.usecase.focushistory.AddFocusHistoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopFocusModeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepositoryImpl,
    private val stopwatchRepository: StopwatchRepositoryImpl,
    private val addFocusHistoryUseCase: AddFocusHistoryUseCase,
    private val deleteBlockedAppUseCase: DeleteBlockedAppUseCase
){
    fun execute(focusModeSessionDuration: FocusModeSessionDuration, activity: String): Flow<RoomResult> = flow {
        val result = deleteBlockedAppUseCase.all().first()

        if(result is RoomResult.Failed){
            emit(RoomResult.Failed(result.message))
            return@flow
        }

        //insert focus mode history session
        val start = focusModeSessionDuration.startMills
        val stop = focusModeSessionDuration.stopMills
        val res = addFocusHistoryUseCase.execute(start, stop, activity).first()

        if(res is RoomResult.Failed){
            emit(RoomResult.Failed(res.message))
            return@flow
        }

        stopwatchRepository.stopStopwatch()
//        delay(100) // Beri delay sebelum unbind untuk memastikan state ter-reset
        stopwatchRepository.unbindService()
        dataStoreRepository.setFocusModeStatus(false) //ini harus di bawah agar benar" stop

        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }
}