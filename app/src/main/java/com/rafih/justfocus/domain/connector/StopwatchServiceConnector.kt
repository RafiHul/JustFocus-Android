package com.rafih.justfocus.domain.connector

import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import kotlinx.coroutines.flow.Flow

interface StopwatchServiceConnector {
    fun startStopwatch()
    fun stopStopwatch()
    fun pauseStopwatch()
    fun setStopwatchDuration(stopwatchDuration: StopwatchDuration)
    fun getStopwatchState(): Flow<StopwatchState>
}