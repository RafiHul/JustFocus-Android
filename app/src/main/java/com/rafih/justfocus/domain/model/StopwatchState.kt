package com.rafih.justfocus.domain.model

data class StopwatchState(
    val isRunning: Boolean,
    val seconds: Int = 0,
    val minutes: Int = 0,
    val hours: Int = 0
){
    fun toMillis(): Long {
        return (hours * 3600 + minutes * 60 + seconds) * 1000L
    }
}