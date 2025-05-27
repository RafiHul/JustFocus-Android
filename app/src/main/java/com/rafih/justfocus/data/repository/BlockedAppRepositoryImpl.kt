package com.rafih.justfocus.data.repository

import android.content.pm.PackageManager
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.model.BlockedApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppRepositoryImpl @Inject constructor(private val dao: BlockedAppDao) {

    private var chachedBlockedList: List<String> = emptyList()

    suspend fun addBlockedApp(blockedApp: BlockedApp) = dao.addBlockedApp(blockedApp)
    suspend fun deleteBlockedApp(blockedApp: BlockedApp) = dao.deleteBlockedApp(blockedApp)

    suspend fun loadBlockedApp(){
        chachedBlockedList = dao.getBlockedApp().map { it.packageName }
    }

    suspend fun fetchBlockedApp(pm: PackageManager): Set<String> {
        return chachedBlockedList.filter {
            try {
                pm.getApplicationInfo(it, 0)
                true
            } catch (_: Exception){
                false
            }
        }.toSet()
    }

    fun isAppBlocked(packageName: String): Boolean {
       return chachedBlockedList.contains(packageName)
    }


}