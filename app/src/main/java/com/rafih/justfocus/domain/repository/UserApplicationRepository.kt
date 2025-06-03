package com.rafih.justfocus.domain.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.rafih.justfocus.domain.model.AppUsageGroup

interface UserApplicationRepository {
    suspend fun fetchWeeklyAppUsage(context: Context, pm: PackageManager): MutableList<AppUsageGroup>
    suspend fun fetchAppInstalled(pm: PackageManager): List<ApplicationInfo?>
    fun getCachedWeeklyUsage(): List<AppUsageGroup>
}