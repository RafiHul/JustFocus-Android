package com.rafih.justfocus.domain.connector

import com.rafih.justfocus.domain.model.StopWatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import kotlinx.coroutines.flow.Flow

interface StopwatchServiceConnector {
    fun startStopwatch()
    fun stopStopwatch()
    fun pauseStopwatch()
    fun resumeStopwatch()
    fun setStopwatchDuration(stopWatchDuration: StopWatchDuration)
    fun getStopwatchState(): Flow<StopwatchState>
}