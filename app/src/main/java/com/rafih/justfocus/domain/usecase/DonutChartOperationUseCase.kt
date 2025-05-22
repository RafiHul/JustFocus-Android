package com.rafih.justfocus.domain.usecase

import androidx.compose.ui.graphics.Color
import com.aay.compose.donutChart.model.PieChartData
import com.rafih.justfocus.domain.model.AppUsageEvent
import com.rafih.justfocus.domain.model.DataChartPercentInfo
import com.rafih.justfocus.domain.model.UsageStatsInfo
import com.rafih.justfocus.presentation.ui.theme.pieChartDataColor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.plusAssign

@Singleton
class DonutChartOperationUseCase @Inject constructor(){
    suspend fun calculatePercentageForDonutChart(data: List<AppUsageEvent>):  List<UsageStatsInfo> {
        val finalData: MutableList<UsageStatsInfo> = mutableListOf()

        val total = data.sumOf { it.appUsedTimeInMills }

        data.forEachIndexed { idx, item ->
            val percent = (item.appUsedTimeInMills.toDouble() / total.toDouble()) * 100
            val dataChartPercentInfo = DataChartPercentInfo(percent, pieChartDataColor.getOrElse(idx) { pieChartDataColor.last() })
            val temp = UsageStatsInfo(item, dataChartPercentInfo)
            finalData.add(temp)
        }

        return finalData
    }

    fun filterDonutChart(data: List<UsageStatsInfo>): MutableList<PieChartData> {

        val (mainData, otherData) = data.partition { it.dataChartPercentInfo.color != Color.Gray } // partition data

        val tempChartData = mutableListOf<PieChartData>()

        tempChartData += mainData.map {
            PieChartData(
                it.dataChartPercentInfo.percentUsed,
                it.dataChartPercentInfo.color,
                it.appUsageEvent.packageName
            )
        }

        val otherStateDataTotal = otherData.sumOf { it.dataChartPercentInfo.percentUsed }.toDouble()
        tempChartData += PieChartData(otherStateDataTotal, Color.Gray, "Other")
        return tempChartData
    }

}