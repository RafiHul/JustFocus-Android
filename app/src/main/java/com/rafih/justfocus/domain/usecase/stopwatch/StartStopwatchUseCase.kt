package com.rafih.justfocus.domain.usecase.stopwatch

import com.rafih.justfocus.data.repository.StopwatchRepositoryImpl
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartStopwatchUseCase @Inject constructor(
    private val stopwatchRepository: StopwatchRepositoryImpl
){
    suspend operator fun invoke() {
        stopwatchRepository.bindService()
        stopwatchRepository.serviceConnected.first { it }
        stopwatchRepository.startStopwatch()
    }
}