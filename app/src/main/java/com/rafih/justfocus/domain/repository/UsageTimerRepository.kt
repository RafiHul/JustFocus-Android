package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.AppUsageLimit
import kotlinx.coroutines.Job

interface UsageTimerRepository {
    var currentAppUsageMillis: Long
    var currentJob: Job?
    fun startTracking(appUsageLimit: AppUsageLimit, callBackBlocked: () -> Unit)
    fun stopTracking()
}