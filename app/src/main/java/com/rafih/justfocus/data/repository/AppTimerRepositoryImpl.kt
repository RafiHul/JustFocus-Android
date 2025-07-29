package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.room.dao.AppTimerDao
import com.rafih.justfocus.data.model.AppTimer
import com.rafih.justfocus.domain.repository.AppTimerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppTimerRepositoryImpl @Inject constructor(private val dao: AppTimerDao): AppTimerRepository {

    override var chachedAppTimer: List<AppTimer> = emptyList()

    override suspend fun addBatchAppTimer(appTimerList: List<AppTimer>) {
        dao.addBatchAppTimer(appTimerList)
        loadAppTimer()
    }

    override suspend fun deleteAppTimer(appTimer: AppTimer) {
        dao.deleteAppTimer(appTimer)
        loadAppTimer()
    }

    override suspend fun loadAppTimer() {
        chachedAppTimer = dao.getAppTimer()
    }


    override fun isAppBlocked(packageName: String, timeMillsNow: Long): Boolean {

        for (appTimer in chachedAppTimer) {
            if (appTimer.packageName == packageName) {
                return timeMillsNow >= appTimer.blockUntilMills
            }
        }
        return false
    }
}