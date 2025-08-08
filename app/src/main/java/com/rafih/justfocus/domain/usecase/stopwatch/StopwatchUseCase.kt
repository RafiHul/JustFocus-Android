package com.rafih.justfocus.domain.usecase.stopwatch

import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.StopWatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchUseCase @Inject constructor(
    val stopWatchRepository: StopwatchRepositoryImpl,
    val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
) {
    suspend fun pauseStopwatch(){
        dataStoreRepositoryImpl.setFocusModeStatus(false)
        stopWatchRepository.pauseStopwatch()
    }

    suspend fun resumeStopWatch(){
        dataStoreRepositoryImpl.setFocusModeStatus(true)
        stopWatchRepository.resumeStopwatch()
    }

    suspend fun setStopWatchDuration(stopWatchDuration: StopWatchDuration){
        stopWatchRepository.serviceConnected.first {it == true}
        stopWatchRepository.setStopwatchDuration(stopWatchDuration)
    }

    suspend fun getStopwatchState(): Flow<StopwatchState> {
        stopWatchRepository.serviceConnected.first {it == true}
        return stopWatchRepository.getStopwatchState()
    }
}