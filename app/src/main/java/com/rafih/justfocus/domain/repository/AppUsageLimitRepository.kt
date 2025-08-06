package com.rafih.justfocus.domain.repository

import com.rafih.justfocus.data.model.AppUsageLimit

interface AppUsageLimitRepository {

    var cachedAppHasUsageLimit: Set<AppUsageLimit>
    var cacheCurrentOpenedPackageName: String?

    suspend fun add(appUsageLimit: AppUsageLimit)
    suspend fun addBatch(appUsageLimitList: List<AppUsageLimit>)
    suspend fun update(appUsageLimit: AppUsageLimit)
    suspend fun delete(appUsageLimit: AppUsageLimit)
    suspend fun deleteByPackageName(packageName: String)
    suspend fun load()
    fun isAppHasUsageLimit(packageName: String, callBackBlocked: () -> Unit, isFromContentChanged: Boolean = false)
}