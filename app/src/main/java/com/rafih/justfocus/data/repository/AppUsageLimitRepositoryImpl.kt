package com.rafih.justfocus.data.repository

import android.util.Log
import com.rafih.justfocus.data.local.room.dao.AppUsageLimitDao
import com.rafih.justfocus.data.model.AppUsageLimit
import com.rafih.justfocus.domain.repository.AppUsageLimitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageLimitRepositoryImpl @Inject constructor(
    private val dao: AppUsageLimitDao,
    private val usageTimerRepository: UsageTimerRepositoryImpl
): AppUsageLimitRepository {

    override var cachedAppHasUsageLimit: Set<AppUsageLimit> = emptySet()
    override var cacheCurrentOpenedPackageName: String? = null

    override suspend fun add(appUsageLimit: AppUsageLimit) {
        dao.add(appUsageLimit)
        load()
    }

    override suspend fun addBatch(appUsageLimitList: List<AppUsageLimit>) {
        dao.addBatch(appUsageLimitList)
        load()
    }

    override suspend fun update(appUsageLimit: AppUsageLimit) {
        dao.update(appUsageLimit)
        load()
    }

    override suspend fun delete(appUsageLimit: AppUsageLimit) {
        dao.delete(appUsageLimit)
        load()
    }

    override suspend fun deleteByPackageName(packageName: String) {
        dao.deleteByPackageName(packageName)
        load()
    }

    override suspend fun load() {
        cachedAppHasUsageLimit = dao.get().toSet()
    }

    override fun isAppHasUsageLimit(
        packageName: String,
        callBackBlocked: () -> Unit,
        isFromContentChanged: Boolean
    ) {
        val appUsageLimit = cachedAppHasUsageLimit.find { it.packageName == packageName }

        checkIsChangeAppAndSave(cacheCurrentOpenedPackageName, packageName, isFromContentChanged)

        if (appUsageLimit != null && cacheCurrentOpenedPackageName == null) {
            cacheCurrentOpenedPackageName = packageName
            usageTimerRepository.currentAppUsageMillis = appUsageLimit.usageMillis
            usageTimerRepository.startTracking(appUsageLimit, callBackBlocked)
        }
    }



    private fun checkIsChangeAppAndSave(
        previousAppPackageName: String?,
        newPackageName: String,
        isFromContentChanged: Boolean
    ) {
        if (previousAppPackageName != newPackageName && !isFromContentChanged) {
            // hanya simpan kalau bukan dari CONTENT_CHANGED
            previousAppPackageName?.let { prevApp ->
                val oldLimit = cachedAppHasUsageLimit.find { it.packageName == prevApp }
                if (oldLimit != null) {
                    usageTimerRepository.stopTracking()

                    val updatedUsage = usageTimerRepository.currentAppUsageMillis
                    CoroutineScope(Dispatchers.IO).launch {
                        update(oldLimit.copy(usageMillis = updatedUsage))
                        usageTimerRepository.currentAppUsageMillis = 0L
                    }
                }

                cacheCurrentOpenedPackageName = null
            }
        }
    }


}