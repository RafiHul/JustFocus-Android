package com.rafih.justfocus.data.repository

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.rafih.justfocus.domain.model.AppUsageEvent
import com.rafih.justfocus.domain.model.AppUsageGroup
import com.rafih.justfocus.domain.repository.UserApplicationRepository
import com.rafih.justfocus.domain.setCalendarTime
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserApplicationRepositoryImpl @Inject constructor(): UserApplicationRepository {

    private var cacheWeeklyAppUsage: MutableList<AppUsageGroup> = mutableListOf()
    private val calendarToday = Calendar.getInstance()

    override suspend fun fetchWeeklyAppUsage(context: Context, pm: PackageManager): MutableList<AppUsageGroup>{
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val weekData = mutableListOf<AppUsageGroup>()

        val dayOfWeekCalendar = Calendar.getInstance()
        dayOfWeekCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeekCalendar.firstDayOfWeek)

        for (i in 0..6) {
            val day = dayOfWeekCalendar.clone() as Calendar
            day.setCalendarTime(0, 0, 0)

            val startTime = day.timeInMillis
            var endTime = System.currentTimeMillis()

            if (day.get(Calendar.DAY_OF_MONTH) != calendarToday.get(Calendar.DAY_OF_MONTH)) {
                day.setCalendarTime(23, 59, 59)
                endTime = day.timeInMillis
            }

            val usageData = mutableListOf<AppUsageEvent>()
            val lastForegroundMap = mutableMapOf<String, Long>()
            val usageMap = mutableMapOf<String, Long>()
            val usageEvents = usageStatsManager.queryEvents(startTime, endTime)

            val event = UsageEvents.Event()
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                val packageName = event.packageName

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        lastForegroundMap[packageName] = event.timeStamp
                    }
                    UsageEvents.Event.ACTIVITY_PAUSED -> {
                        val start = lastForegroundMap[packageName] ?: continue
                        val duration = event.timeStamp - start
                        if (duration > 0) {
                            usageMap[packageName] = usageMap.getOrDefault(packageName, 0L) + duration
                        }
                        lastForegroundMap.remove(packageName)
                    }
                }
            }

            usageMap.forEach { (pkg, time) ->
                usageData.add(AppUsageEvent(pkg, time))
            }

            val filteredUsage = usageData
                .filter { isUserOrUserFacingSystemApp(pm, it.packageName) }
                .sortedByDescending { it.appUsedTimeInMills }

            weekData.add(AppUsageGroup(day, filteredUsage, i))
            dayOfWeekCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        cacheWeeklyAppUsage = weekData
        return weekData
    }

    override suspend fun fetchAppInstalled(pm: PackageManager): List<ApplicationInfo?> {
        return pm.getInstalledApplications(PackageManager.MATCH_ALL)
            .filter { isUserOrUserFacingSystemApp(pm, it.packageName) }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
    }

    /**
     * Checks if an application is either a user-installed app or a user-facing system app.
     *
     * User-facing system apps are defined as system apps that have a launcher icon and are not
     * part of a predefined list of core system packages (e.g., Settings, SystemUI).
     *
     * @param pm The PackageManager instance to query application information.
     * @param packageName The package name of the application to check.
     * @return `true` if the application is a user app or a user-facing system app, `false` otherwise.
     *         Returns `false` if the package name is not found.
     */
    fun isUserOrUserFacingSystemApp(pm: PackageManager, packageName: String): Boolean {
        return try {
            val appInfo = pm.getApplicationInfo(packageName, 0)

            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            val isUserApp = !isSystemApp

            val hasLauncher = pm.queryIntentActivities(
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                    .setPackage(packageName),
                PackageManager.MATCH_ALL
            ).isNotEmpty()

            val coreSystemPackages = setOf(
                "com.android.settings",
                "com.android.systemui",
                "com.android.phone",
                "com.android.carrierconfig",
                "com.android.permissioncontroller"
            )

            (isUserApp || (isSystemApp && hasLauncher && packageName !in coreSystemPackages))
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun getCachedWeeklyUsage(): List<AppUsageGroup> {
        return cacheWeeklyAppUsage
    }

}