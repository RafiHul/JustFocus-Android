package com.rafih.justfocus.domain.usecase.stopwatch

import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.data.repository.FocusHistoryRepositoryImpl
import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.FocusModeSessionInfo
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
    private val deleteBlockedAppUseCase: DeleteBlockedAppUseCase,
    private val focusHistoryRepositoryImpl: FocusHistoryRepositoryImpl
){
    fun execute(): Flow<RoomResult> = flow {
        val result = deleteBlockedAppUseCase.all().first()
        val stopwatchState = stopwatchRepository.getStopwatchState().first()

        if(result is RoomResult.Failed){
            emit(RoomResult.Failed(result.message))
            return@flow
        }

        //jadi ini agar akurat dia stop nya, bukan di lihat dari waktu saat ini, tapi di tambah dari waktu pada timer yang tampil
        val focusModeSessionInfo = focusHistoryRepositoryImpl.cacheFocusModeSessionInfo!!
        val stopTimeMills = focusModeSessionInfo.startMills + stopwatchState.toMillis()
        focusModeSessionInfo.stopMills = stopTimeMills

        val res = addFocusHistoryUseCase.execute(focusModeSessionInfo).first()

        if(res is RoomResult.Failed){
            emit(RoomResult.Failed(res.message))
            return@flow
        }

        focusHistoryRepositoryImpl.setCacheFocusMode(null) //back to null
        stopwatchRepository.stopStopwatch()
//        delay(100) // Beri delay sebelum unbind untuk memastikan state ter-reset
        stopwatchRepository.unbindService()
        dataStoreRepository.setFocusModeStatus(false) //ini harus di bawah agar benar" stop

        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }
}