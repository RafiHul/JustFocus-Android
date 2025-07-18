package com.rafih.justfocus.domain.repository

import android.content.pm.PackageManager
import com.rafih.justfocus.data.model.BlockedApp

interface BlockedAppRepository {

    var chachedBlockedAppPackageName: List<String>

    suspend fun addBlockedApp(blockedApp: BlockedApp)
    suspend fun addBatchBlockedApp(blockedAppList: List<BlockedApp>)
    suspend fun deleteAllBlockedApp()
    suspend fun loadBlockedApp()
    suspend fun fetchBlockedApp(pm: PackageManager): Set<String>
    fun isAppBlocked(packageName: String): Boolean


}