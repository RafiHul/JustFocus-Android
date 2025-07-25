package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.BlockedShort

interface BlockedShortRepository {
    var chachedBlockedShortPackageName: List<String>

    suspend fun addBatchBlockedShort(blockedAppList: List<BlockedShort>)
    suspend fun deleteAllBlockedShort()
    suspend fun loadBlockedShort()
    fun isAppBlocked(packageName: String): Boolean
}