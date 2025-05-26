package com.rafih.justfocus.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.rafih.justfocus.domain.model.AppUsageGroup

interface UsageStatsRepository {
    suspend fun getWeeklyUsageStats(context: Context, pm: PackageManager): MutableList<AppUsageGroup>
}