package com.rafih.justfocus.domain.usecase

import android.app.usage.UsageStats
import com.rafih.justfocus.domain.model.DataChartPercentInfo
import com.rafih.justfocus.domain.model.UsageStatsInfo
import com.rafih.justfocus.presentation.ui.theme.pieChartDataColor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculateAppUsagePercentageUseCase @Inject constructor(){
    suspend fun calculatePercentages(data: List<UsageStats>?):  List<UsageStatsInfo> {
        if (data == null) return listOf()
        val finalData: MutableList<UsageStatsInfo> = mutableListOf()

        val total = data.sumOf { it.totalTimeInForeground }

        data.forEachIndexed { idx, item ->
            val percent = (item.totalTimeInForeground.toDouble() / total.toDouble()) * 100
            val dataChartPercentInfo = DataChartPercentInfo(percent, pieChartDataColor.getOrElse(idx) { pieChartDataColor.last() })
            val temp = UsageStatsInfo(item, dataChartPercentInfo)
            finalData.add(temp)
        }

        return finalData
    }

}