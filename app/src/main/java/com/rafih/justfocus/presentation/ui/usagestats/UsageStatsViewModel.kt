package com.rafih.justfocus.presentation.ui.usagestats

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UsageStatsViewModel @Inject constructor(): ViewModel() {

    var usageStatsState by mutableStateOf<UsageStatsState>(UsageStatsState.Loading)
        private set

    private val _usageStatsData = MutableStateFlow<List<UsageStats>?>(null)
    val usageStatsData: StateFlow<List<UsageStats>?> = _usageStatsData

    fun loadUsageStatsData(context: Context, pm: PackageManager){
        viewModelScope.launch {
            _usageStatsData.value = getUserSystemUsage(context, pm)
        }
    }

    private suspend fun getUserSystemUsage(context: Context, pm: PackageManager): List<UsageStats>? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val endTime = System.currentTimeMillis()
//      val startTime = endTime - 100 * 60 * 60 * 24 //24 jam
        val startTime = calendar.timeInMillis

        val usageData = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        //filter system app and app dont exist in device
        val n = usageData.filter {
            try{
                val appInfo = pm.getApplicationInfo(it.packageName, 0)
//                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }.sortedByDescending { it.totalTimeInForeground  }

        usageStatsState = UsageStatsState.Idle
        return n
    }

    sealed class UsageStatsState{
        object Loading: UsageStatsState()
        object Idle: UsageStatsState()
    }
}