package com.rafih.justfocus.domain.usecase.stopwatch

import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import com.rafih.justfocus.domain.usecase.blockedapp.DeleteBlockedAppUseCase
import com.rafih.justfocus.domain.util.RoomResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUseCase @Inject constructor(
    val stopWatchRepository: StopwatchRepositoryImpl,
    val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    val deleteBlockedAppUseCase: DeleteBlockedAppUseCase
) {
    suspend fun pauseStopwatch(){
        dataStoreRepositoryImpl.setFocusModeStatus(false)
        stopWatchRepository.pauseStopwatch()
    }

    fun stopStopwatch(): Flow<RoomResult> = flow {
        val result = deleteBlockedAppUseCase.all().first()

        if(result is RoomResult.Failed){
            emit(RoomResult.Failed(result.message))
            return@flow
        }

        dataStoreRepositoryImpl.setFocusModeStatus(false)
        stopWatchRepository.stopStopwatch()

        delay(100) // Beri delay sebelum unbind untuk memastikan state ter-reset
        stopWatchRepository.unbindService()

        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }

    suspend fun setStopWatchDuration(stopWatchDuration: StopwatchDuration){
        stopWatchRepository.serviceConnected.first {it == true}
        stopWatchRepository.setStopwatchDuration(stopWatchDuration)
    }

    suspend fun getStopwatchState(): Flow<StopwatchState> {
        stopWatchRepository.serviceConnected.first {it == true}
        return stopWatchRepository.getStopwatchState()
    }
}