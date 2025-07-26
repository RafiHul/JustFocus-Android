package com.rafih.justfocus.data.repository

import android.content.pm.PackageManager
import com.rafih.justfocus.data.local.room.dao.BlockedAppDao
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.domain.repository.BlockedAppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppRepositoryImpl @Inject constructor(private val dao: BlockedAppDao): BlockedAppRepository {

    override var chachedBlockedAppPackageName: List<String> = emptyList()

    override suspend fun addBlockedApp(blockedApp: BlockedApp){
        dao.addBlockedApp(blockedApp)
        loadBlockedApp()
    }

    override suspend fun addBatchBlockedApp(blockedAppList: List<BlockedApp>) {
        dao.addBatchBlockedApp(blockedAppList)
        loadBlockedApp()
    }

    // TODO: ini gk di load pas delete ??
    override suspend fun deleteAllBlockedApp() = dao.deleteAllBlockedApp()

    override suspend fun loadBlockedApp(){
        chachedBlockedAppPackageName = dao.getBlockedApp().map { it.packageName }
    }

    override suspend fun fetchBlockedApp(pm: PackageManager): Set<String> {
        return chachedBlockedAppPackageName.filter {
            try {
                pm.getApplicationInfo(it, 0)
                true
            } catch (_: Exception){
                false
            }
        }.toSet()
    }

    override fun isAppBlocked(packageName: String): Boolean {
       return chachedBlockedAppPackageName.contains(packageName)
    }
}