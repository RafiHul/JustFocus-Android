package com.rafih.justfocus.data.repository

import android.content.pm.PackageManager
import android.util.Log
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.domain.repository.BlockedAppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppRepositoryImpl @Inject constructor(private val dao: BlockedAppDao): BlockedAppRepository {

    override var chachedBlockedList: List<String> = emptyList()

    override suspend fun addBlockedApp(blockedApp: BlockedApp){
        dao.addBlockedApp(blockedApp)
        loadBlockedApp()
    }
    override suspend fun deleteAllBlockedApp() = dao.deleteAllBlockedApp()

    override suspend fun loadBlockedApp(){
        chachedBlockedList = dao.getBlockedApp().map { it.packageName }
    }

    override suspend fun fetchBlockedApp(pm: PackageManager): Set<String> {
        return chachedBlockedList.filter {
            try {
                pm.getApplicationInfo(it, 0)
                true
            } catch (_: Exception){
                false
            }
        }.toSet()
    }

    override fun isAppBlocked(packageName: String): Boolean {
       return chachedBlockedList.contains(packageName)
    }
}