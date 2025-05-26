package com.rafih.justfocus.domain.usecase

import android.content.Context
import android.content.pm.PackageManager
import com.rafih.justfocus.data.repository.UsageStatsRepository
import com.rafih.justfocus.domain.model.AppUsageGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsUseCase @Inject constructor(
    private val repo: UsageStatsRepository
){
    suspend fun getWeeklyUsageStats(context: Context, pm: PackageManager): MutableList<AppUsageGroup> {
        return repo.getWeeklyUsageStats(context, pm)
    }
}