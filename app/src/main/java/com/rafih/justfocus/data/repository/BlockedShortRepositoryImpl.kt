package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.room.dao.BlockedShortDao
import com.rafih.justfocus.data.model.BlockedShort
import com.rafih.justfocus.domain.repository.BlockedShortRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedShortRepositoryImpl @Inject constructor(private val dao: BlockedShortDao): BlockedShortRepository{
    override var chachedBlockedShortPackageName: List<String> = emptyList()

    override suspend fun addBatchBlockedShort(blockedShortList: List<BlockedShort>) {
        dao.addBatchBlockedShort(blockedShortList)
        loadBlockedShort()
    }

    override suspend fun deleteAllBlockedShort() {
        dao.deleteAllBlockedShort()
    }

    override suspend fun loadBlockedShort() {
        chachedBlockedShortPackageName = dao.getBlockedShort().map { it.packageName }
    }

    override fun isAppBlocked(packageName: String): Boolean {
        return chachedBlockedShortPackageName.contains(packageName)
    }
}