package com.rafih.justfocus.presentation.ui.usagestats

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aay.compose.donutChart.model.PieChartData
import com.rafih.justfocus.domain.model.AppUsageEvent
import com.rafih.justfocus.domain.model.UsageStatsInfo
import com.rafih.justfocus.domain.usecase.DonutChartOperationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UsageStatsViewModel @Inject constructor(
    private val donutChartUseCase: DonutChartOperationUseCase
): ViewModel() {

    var usageStatsState by mutableStateOf<UsageStatsState>(UsageStatsState.Loading)
        private set

    private val _usageStatsData = MutableStateFlow<List<UsageStatsInfo>?>(null)
    val usageStatsData: StateFlow<List<UsageStatsInfo>?> = _usageStatsData

    private val _donutChartData = MutableStateFlow<List<PieChartData>>(emptyList())
    var donutChartData: StateFlow<List<PieChartData>?> = _donutChartData

    fun loadUsageStatsData(context: Context, pm: PackageManager){
        viewModelScope.launch {
            _usageStatsData.value = getUserSystemUsage(context, pm)
        }
    }

    private suspend fun getUserSystemUsage(context: Context, pm: PackageManager): List<UsageStatsInfo>? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val lastForegroundMap: MutableMap<String, Long> = mutableMapOf()
        val usageMap: MutableMap<String, Long> = mutableMapOf()
        val usageData: MutableList<AppUsageEvent> = mutableListOf()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

//      val startTime = endTime - 100 * 60 * 60 * 24 //24 jam
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)

        val event = UsageEvents.Event()
        while(usageEvents.hasNextEvent()){
            usageEvents.getNextEvent(event)

            val packageName = event.packageName

            when(event.eventType){
                UsageEvents.Event.ACTIVITY_RESUMED -> { // aplikasi terakhir di buka kapan
                    lastForegroundMap[packageName] = event.timeStamp
                }
                UsageEvents.Event.ACTIVITY_PAUSED -> { // aplikasi terakhir di tutup kapan
                    val start = lastForegroundMap[packageName] ?: continue
                    val duration = event.timeStamp - start
                    if (duration > 0){
                        usageMap[packageName] = usageMap.getOrDefault(packageName, 0L) + duration
                    }

                    lastForegroundMap.remove(packageName)
                }
            }
        }

        usageMap.forEach { k,v ->
            usageData.add(AppUsageEvent(k,v))
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

        val x = donutChartUseCase.calculatePercentageForDonutChart(n)

        _donutChartData.value = donutChartUseCase.filterDonutChart(x)
        usageStatsState = UsageStatsState.Idle
        return x
    }

    sealed class UsageStatsState{
        object Loading: UsageStatsState()
        object Idle: UsageStatsState()
    }
}