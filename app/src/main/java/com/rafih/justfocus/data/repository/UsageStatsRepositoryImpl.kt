package com.rafih.justfocus.data.repository

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.rafih.justfocus.domain.model.AppUsageEvent
import com.rafih.justfocus.domain.model.AppUsageGroup
import com.rafih.justfocus.setCalendarTime
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsRepositoryImpl @Inject constructor(): UsageStatsRepository{

    private val calendarToday = Calendar.getInstance()

    override suspend fun getWeeklyUsageStats(context: Context, pm: PackageManager): MutableList<AppUsageGroup> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val weekData = mutableListOf<AppUsageGroup>()

        val dayOfWeekCalendar = Calendar.getInstance()
        dayOfWeekCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeekCalendar.firstDayOfWeek)

        for (i in 0..6) {
            val day = dayOfWeekCalendar.clone() as Calendar
            day.setCalendarTime(0, 0, 0)

            var startTime = day.timeInMillis
            var endTime = System.currentTimeMillis() //this time and today

            if (day.get(Calendar.DAY_OF_MONTH) != calendarToday.get(Calendar.DAY_OF_MONTH)) { //if not today
                day.setCalendarTime(23, 59, 59)
                endTime = day.timeInMillis
            }

            //      val startTime = endTime - 100 * 60 * 60 * 24 //24 jam
            val usageData: MutableList<AppUsageEvent> = mutableListOf()
            val lastForegroundMap: MutableMap<String, Long> = mutableMapOf()
            val usageMap: MutableMap<String, Long> = mutableMapOf()
            val usageEvents = usageStatsManager.queryEvents(startTime, endTime)

            val event = UsageEvents.Event()
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)

                val packageName = event.packageName

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> { // aplikasi terakhir di buka kapan
                        lastForegroundMap[packageName] = event.timeStamp
                    }

                    UsageEvents.Event.ACTIVITY_PAUSED -> { // aplikasi terakhir di tutup kapan
                        val start = lastForegroundMap[packageName] ?: continue
                        val duration = event.timeStamp - start
                        if (duration > 0) {
                            usageMap[packageName] =
                                usageMap.getOrDefault(packageName, 0L) + duration
                        }

                        lastForegroundMap.remove(packageName)
                    }
                }
            }

            usageMap.forEach { k, v ->
                usageData.add(AppUsageEvent(k, v))
            }

            val n = usageData.filter {
                try {
                    pm.getApplicationInfo(it.packageName, 0)
//                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
                    true
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }
            }.sortedByDescending { it.appUsedTimeInMills }

            val temp = AppUsageGroup(day, n, i)
            weekData.add(temp)

            dayOfWeekCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weekData
    }

}