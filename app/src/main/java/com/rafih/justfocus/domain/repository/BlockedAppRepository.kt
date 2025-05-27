package com.rafih.justfocus.domain.repository

import android.content.pm.PackageManager
import com.rafih.justfocus.data.model.BlockedApp

interface BlockedAppRepository {

    var chachedBlockedList: List<String>

    suspend fun addBlockedApp(blockedApp: BlockedApp)
    suspend fun deleteBlockedApp(blockedApp: BlockedApp)
    suspend fun loadBlockedApp()
    suspend fun fetchBlockedApp(pm: PackageManager): Set<String>

    fun isAppBlocked(packageName: String): Boolean


}