package com.rafih.justfocus.domain.usecase

import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import com.rafih.justfocus.domain.model.StopwatchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StopwatchUseCase @Inject constructor(
    val stopWatchRepository: StopwatchRepositoryImpl
) {
    suspend fun startStopwatch(){
        stopWatchRepository.bindService()

        stopWatchRepository.serviceConnected.collect {
            if (it){
                stopWatchRepository.startStopwatch()
            }
        }

    }

    fun pauseStopwatch(){
        stopWatchRepository.pauseStopwatch()
    }

    fun stopStopwatch(){
        stopWatchRepository.stopStopwatch()
        stopWatchRepository.unbindService()
    }

    suspend fun getStopwatchState(): Flow<StopwatchState> {
        stopWatchRepository.serviceConnected.first {it == true}

        return stopWatchRepository.getStopwatchState()
    }
}