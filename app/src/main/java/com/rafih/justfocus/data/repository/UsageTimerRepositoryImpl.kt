package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.model.AppUsageLimit
import com.rafih.justfocus.domain.repository.UsageTimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageTimerRepositoryImpl @Inject constructor() : UsageTimerRepository {
    override var currentAppUsageMillis: Long = 0L
    override var currentJob: Job? = null
    private var trackingStartTime: Long = 0L

    override fun startTracking(appUsageLimit: AppUsageLimit, callBackBlocked: () -> Unit) {
        currentJob?.cancel()

        trackingStartTime = System.currentTimeMillis()
        currentJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(1000)
                val elapsed = System.currentTimeMillis() - trackingStartTime
                val totalUsage = appUsageLimit.usageMillis + elapsed
                currentAppUsageMillis = totalUsage

                if (totalUsage >= appUsageLimit.limitMillis) {
                    callBackBlocked()
                }
            }
        }
    }

    override fun stopTracking() {
        currentJob?.cancel()
        val elapsed = System.currentTimeMillis() - trackingStartTime
        currentAppUsageMillis = currentAppUsageMillis.coerceAtLeast(elapsed)
    }
}