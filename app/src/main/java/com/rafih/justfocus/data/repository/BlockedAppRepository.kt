package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.model.BlockedApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppRepository @Inject constructor(private val dao: BlockedAppDao) {

    private var chachedBlockedList: List<String> = emptyList()

    suspend fun addBlockedApp(blockedApp: BlockedApp) = dao.addBlockedApp(blockedApp)
    suspend fun deleteBlockedApp(blockedApp: BlockedApp) = dao.deleteBlockedApp(blockedApp)

    suspend fun loadBlockedApp(){
        chachedBlockedList = dao.getBlockedApp().map { it.packageName }
    }

    fun isAppBlocked(packageName: String): Boolean {
       return chachedBlockedList.contains(packageName)
    }


}