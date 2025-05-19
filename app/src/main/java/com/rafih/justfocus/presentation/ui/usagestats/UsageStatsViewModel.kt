package com.rafih.justfocus.presentation.ui.usagestats

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aay.compose.donutChart.model.PieChartData
import com.rafih.justfocus.domain.model.UsageStatsInfo
import com.rafih.justfocus.domain.usecase.CalculateAppUsagePercentageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UsageStatsViewModel @Inject constructor(
    private val donutChartUseCase: CalculateAppUsagePercentageUseCase
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

        val n = usageData.filter {
            try{
                pm.getApplicationInfo(it.packageName, 0)
//                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }.sortedByDescending { it.totalTimeInForeground }

        val x = donutChartUseCase.calculatePercentages(n)

        _donutChartData.value = filterDonutChart(x)
        usageStatsState = UsageStatsState.Idle
        return x
    }

    private fun filterDonutChart(data: List<UsageStatsInfo>): MutableList<PieChartData> {

        val (mainData, otherData) = data.partition { it.dataChartPercentInfo.color != Color.Gray } // partition data

        val tempChartData = mutableListOf<PieChartData>()

        tempChartData += mainData.map {
            PieChartData(
                it.dataChartPercentInfo.percentUsed,
                it.dataChartPercentInfo.color,
                it.usageStats.packageName
            )
        }

        val otherStateDataTotal = otherData.sumOf { it.dataChartPercentInfo.percentUsed }.toDouble()
        tempChartData += PieChartData(otherStateDataTotal, Color.Gray, "Other")
        return tempChartData
    }

    sealed class UsageStatsState{
        object Loading: UsageStatsState()
        object Idle: UsageStatsState()
    }
}