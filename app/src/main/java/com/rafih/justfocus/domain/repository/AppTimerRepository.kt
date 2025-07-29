package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.AppTimer

interface AppTimerRepository {

    var chachedAppTimer: List<AppTimer>

    suspend fun addBatchAppTimer(appTimerList: List<AppTimer>)
    suspend fun deleteAppTimer(appTimer: AppTimer)
    suspend fun loadAppTimer()
    fun isAppBlocked(packageName: String, timeMillsNow: Long): Boolean
}