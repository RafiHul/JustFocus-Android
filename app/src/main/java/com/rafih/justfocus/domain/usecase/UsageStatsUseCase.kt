package com.rafih.justfocus.domain.usecase

import android.content.Context
import android.content.pm.PackageManager
import com.rafih.justfocus.data.repository.UserApplicationRepositoryImpl
import com.rafih.justfocus.domain.repository.UserApplicationRepository
import com.rafih.justfocus.domain.model.AppUsageGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsUseCase @Inject constructor(
    private val repo: UserApplicationRepositoryImpl
){
    suspend fun fetchWeeklyAppUsage(context: Context, pm: PackageManager): MutableList<AppUsageGroup> {
        return repo.fetchWeeklyAppUsage(context, pm)
    }

    fun getCachedWeeklyUsage(): List<AppUsageGroup> {
        return repo.getCachedWeeklyUsage()
    }
}