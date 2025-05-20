package com.rafih.justfocus.data.local.repository

import com.rafih.justfocus.data.local.dao.BlockedAppDao

class BlockedAppRepository(private val dao: BlockedAppDao) {

    private var chachedBlockedList: List<String> = emptyList()

    suspend fun loadBlockedApps(){
        chachedBlockedList = dao.getBlockedApp().map { it.packageName }
    }

    fun isAppBlocked(packageName: String): Boolean {
       return chachedBlockedList.contains(packageName)
    }

}