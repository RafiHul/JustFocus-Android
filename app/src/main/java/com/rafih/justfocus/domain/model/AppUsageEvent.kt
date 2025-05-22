package com.rafih.justfocus.domain.model

data class AppUsageEvent(
    val packageName: String,
    val appUsedTimeInMills: Long
)
